package com.newtec.rpc.cluster;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;

import com.newtec.myqdp.hardware.GatewayUtils;
import com.newtec.myqdp.hardware.PingUtils;
import com.newtec.myqdp.print.utils.Print;
import com.newtec.myqdp.server.utils.StringUtils;
import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.reflect.annotation.RpcClass;
import com.newtec.reflect.annotation.RpcMethod;
import com.newtec.rpc.call.CallManager;
import com.newtec.rpc.cluster.process.ClusterTransType;
import com.newtec.rpc.cluster.util.ClusterMessage;
import com.newtec.rpc.cluster.util.ClusterMessageType;
import com.newtec.rpc.core.RPCParam;
import com.newtec.rpc.core.SystemData;
import com.newtec.rpc.db.DBManager;
import com.newtec.rpc.db.DBexecuteVoid;
import com.newtec.rpc.monitor.ClusterLogManager;
import com.newtec.rpc.monitor.MonitorManager;
import com.newtec.rpc.monitor.MonitorNode;
import com.newtec.rpc.network.NetWorkManager;
import com.newtec.rpc.node.ClientInfo;
import com.newtec.rpc.node.Node;
import com.newtec.rpc.node.NodeInfo;
import com.newtec.rpc.node.NodeUtils;
import com.newtec.rpc.node.ServerInfo;
import com.newtec.rpc.utils.RpcUtils;


/**
 * @author Yuexin
 * @Description 集群管理模块
 * 				1、加入集群
 * 				启动时，从配置的ip地址中获取指定集群节点，并进行请求加入。
 * 				客户端模式：加入成功，初始化集群信息，并访问剩下的服务端节点，获取服务端节点返回的服务端列表信息。
 * 				服务端模式:加入成功，初始化集群信息，并访问剩下的服务端和客户端端节点，获取节点返回的服务端和客户端列表信息。
 * 				加入失败，循环等待，等待指定地址可用或者有远程节点访问该节点。
 * 				2、更新集群
 * 				运行中，根据自己维护的集群列表，一个个访问，通过策略（本地节点和远程节点互相发送自己的节点信息）来更新节点信息。
 * 				3、更新集群出错 
 * 				如果某个节点无法访问（因本地网络原因或者远程节点网络原因），将从本地维护的集群列表中删除该节点。
 * @date 2017年8月7日
 * @version 1.0
 */
@RpcClass(value = "clusterManager", desc = "集群管理")
public class ClusterManager {
	
	/** 集群节点列表 */
	static private Map<String, ServerInfo> serverInfos = new ConcurrentHashMap<String, ServerInfo>();
	/** 客户节点列表 */
	static private Map<String, ClientInfo> clientInfos = new ConcurrentHashMap<String, ClientInfo>();
	/** 判断是否需要重新加入 */
	static private boolean reJoin = false;
	/** 存放已经发过加入消息的节点，下次发送之前要清空 */
	static private List<String> joinedList = new ArrayList<String>();

	static public String reJoinRemak = ClusterDataParam.startJoin;
	/** 集群更新时间*/
	static private int clusterUpdateTime;
	
	static private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100, 60L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	/**
	 * 方法说明 ：启动集群管理器
	 */
	@RpcMethod(load = false)
	static public void start() throws CustomException {

		clusterUpdateTime = RPCParam.getClusterUpdateTime();

		if (!RPCParam.isClientMode()) {
			Node.getServerInfo().updateRunInfo();
		}

		ClusterClientImpl.start();
		ClusterServerImpl.start();

		if(RPCParam.isStartCluster()) {
			sendCluster();
			startUpdateInfo();
			Print.info(SystemData.ModelCluster + " 集群模块启动成功");
		}

	}

	/**
	 * 方法说明：启动节点集群更新和节点负载更新线程
	 */
	@RpcMethod(load = false)
	static private void startUpdateInfo() {
		final ClusterMessageType updateType = RPCParam.isClientMode() ? ClusterMessageType.CLIENT_UPDATE
				: ClusterMessageType.SERVER_UPDATE;
		/** 加入集群，运行中提示集群信息 */
		ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
		/** 更新集群信息 */
		final StringBuffer serverInfosBuffer = new StringBuffer();
		final StringBuffer joinBuffer = new StringBuffer();
		final StringBuffer updateChannelBuffer = new StringBuffer();
		final int count = clusterUpdateTime <= 30 ? 6 : 1;// 判断集群更新时间是否小于30，来决定重新加入集群的周期
		service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				Print.info(SystemData.ModelCluster + "【定时更新】  当前连接服务端数量 :" + serverInfos.size() + " 分别为:"
						+ serverInfos.keySet());
				if (!RPCParam.isClientMode()) {
					Print.info(SystemData.ModelCluster + "【定时更新】 当前连接客户端数量 :" + clientInfos.size() + " 分别为:"
							+ clientInfos.keySet());
				}
				try {
					if (reJoin == true) {
						//重新加入集群
						if (joinBuffer.length() < 1) {
							Print.info(SystemData.ModelCluster + "【定时更新】 重新发送加入请求");
							joinCluster();
							joinBuffer.append("1");
						} else if (joinBuffer.length() < count) {
							joinBuffer.append("1");
						} else {
							joinBuffer.delete(0, joinBuffer.length());
						}
					} else {
						//定时更新集群
						updateCluster(updateType);
						if (serverInfos.isEmpty()) {
							// 服务器集群列表为空，判断是否自身断网，如果自身断网，设置重新加入标志，重新加入集群
//							if (isHostPortAble(ParamManager.dburl)) {
							if(GatewayUtils.getGateway(NodeUtils.getLocHost()) == null) {
								if (serverInfosBuffer.length() < count + 1) {// 定时周期小于3次忽略
									serverInfosBuffer.append("1");
									return;
								}
								Print.error(SystemData.ModelCluster + " 自身断网，重新加入集群");
								serverInfosBuffer.delete(0, serverInfosBuffer.length());
								reJoin = true;
							}
						} else {
							//不是自身断网，更新集群信息
							CallManager.initChannel();
							if (updateChannelBuffer.length() < count) {
								updateChannelBuffer.append("1");
								return;
							}
							updateChannelBuffer.delete(0, updateChannelBuffer.length());
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
					Print.error(SystemData.ModelCluster + "【定时更新】 出现异常,具体异常信息:" + e.getMessage());
				}

			}
		}, 1, clusterUpdateTime, TimeUnit.SECONDS);

		service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				
//				if (PingUtils.ping(Node.getNodeInfo().getDefaultGateway())) {// 通过ping默认网关的方式，判断自身是否断网，但是遇到路由器禁止ping的情况，哪怕没断网，依然ping不通
				try {
					if (GatewayUtils.getGateway(NodeUtils.getLocHost()) != null) { // 通过获取的默认网关是否为null，判断自身是否断网
						if (RPCRunStatus.networkConnct == false) {// 连接上网络
							RPCRunStatus.networkConnct = true;
							try {
								Print.error(SystemData.ModelCluster + "连接上网络，重新加入集群 "+GatewayUtils.getGateway(NodeUtils.getLocHost()));
								joinCluster();
							} catch (Exception e) {
								e.printStackTrace();
							}
							NetWorkManager.connect();
						}
						for(ServerInfo node :serverInfos.values()) {
							if(PingUtils.ping(node.getHost())==false) {
								removeNode(node.getAddress());
							}
						}
					} else {
						Print.error(SystemData.ModelCluster + " 获取默认网关为null :" + NodeUtils.getLocHost() +" 失败,设置断网状态");
						if (RPCRunStatus.networkConnct) {// 断网
							RPCRunStatus.networkConnct = false;
							System.err.println("获取默认网关为空重新加入集群 "+GatewayUtils.getGateway(NodeUtils.getLocHost())+"|"+NodeUtils.getLocHost());
							ClusterManager.setReJoin(true, ClusterDataParam.netWorkJoin);
							NetWorkManager.disconnect();
						}
					}
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
				Boolean network = null;
				try {
//					network = isHostPortAble(ParamManager.dburl); // 检测数据库是否在线，通过判断端口号方式会出现数据库锁IP异常
					network = RpcUtils.isDbOnline(DBManager.getDBUrl(),DBManager.getDBUser(),DBManager.getDBPass());
//					network = true;
				} catch (Throwable e) {
					e.printStackTrace();
				}
				if (network != null) {
					if (network == false && RPCRunStatus.managerDBConnect) {// db断网
						Print.error(SystemData.ModelCluster + " 数据库连接中断:" + RPCParam.getDbUrl() +" ,设置断网状态");
						RPCRunStatus.managerDBConnect = false;
						NetWorkManager.dbDisconnect();
					} else if (network == true && RPCRunStatus.managerDBConnect == false) {// db联网
						RPCRunStatus.managerDBConnect = true;
						NetWorkManager.dbConnect();
					}

				}
			}
		}, 1, RPCParam.getReconncetTime(), TimeUnit.SECONDS);
		
		if (!RPCParam.isClientMode()) {
			/** 更新运行信息 */
			service.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					try {
						Node.getServerInfo().updateRunInfo();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}, 1, clusterUpdateTime, TimeUnit.SECONDS);
		}

	}

	/**
	 * 方法说明:  网络断开相关处理
	 * @throws CustomException 
	 */
	static public void disconnect(String addr) {
		Boolean isConnect = null;
		try {
			isConnect = ClusterManager.isHostPortAble(addr);
		} catch (CustomException e) {
			e.printStackTrace();
			return;
		}
		if (isConnect != null) {
			// 如果因为对方原因，移除节点
			ClusterManager.removeNode(addr);
		} else if (isConnect == null) {
			// 如果因为自身原因，清理集群，设置重新加入标志
			System.err.println("某个节点断开重新加入集群"+"addr=>"+addr+" ==>"+isConnect);
			ClusterManager.setReJoin(true, ClusterDataParam.netWorkJoin);
		}
	}

	/**
	 * 方法说明:  提供外部调用，重新加入集群的操作
	 */
	static public void joinCluster() throws CustomException {
		Print.error(SystemData.ModelCluster + " 重新加入集群");
		//DBManager.reInitEntityManager();
		// 发送之前要做的操作
		joinedList.clear();
		// 发送进行的操作
		DBManager.executeSuccess(new DBexecuteVoid() {
			@Override
			public void execute(EntityManager manager) throws CustomException {
				Node.reStart();
			}
		}, null);
		DBManager.executeSuccess(new DBexecuteVoid() {
			@Override
			public void execute(EntityManager manager) throws CustomException {
				sendCluster();
			}
		}, null);
		// Node.reStart();
		// sendCluster();
		// 发送完后要做的操作
		setReJoin(false, "");
	}

	/**
	 * 方法说明:  发送加入集群信息，从数据库中读取可以连接的节点并发送加入请求
	 */
	static private void sendCluster() throws CustomException {
		final ClusterMessageType type = RPCParam.isClientMode() ? ClusterMessageType.CLIENT_JOIN
				: ClusterMessageType.SERVER_JOIN;
		final List<MonitorNode> list = new ArrayList<MonitorNode>();

		@SuppressWarnings("unchecked")
		List<MonitorNode> dBlist = DBManager.get()
				.createQuery("from MonitorNode where remove = 0 order by startTime desc ").getResultList();// 为了找集群端口
		list.addAll(dBlist);
		// CountDownLatch con = new CountDownLatch(list.size());
		// long start = System.currentTimeMillis();
		for (MonitorNode node : list) {
			String address = node.getClusterAddr();
			if (RPCParam.getClusterAddr().equals(address)) {
				// con.countDown();
				continue;
			}

			NodeInfo nodeInfo = new NodeInfo(node.getHost(), node.getPort(), node.getCallPort(), node.getName(),
					node.getCpuId(), node.getMac(), node.getMotherboard());
			ClusterManager.submit(new ClusterRunable(nodeInfo, type));

		}
		// 加入集群时，通知客户端列表
		for (ClientInfo clientInfo : clientInfos.values()) {
			ClusterManager.submit(new ClusterRunable(clientInfo, type));
		}

	}

	/**
	 * 方法说明:  提供外部调用，重新加入集群
	 */
	@RpcMethod(load = true, desc = "重新加入集群")
	static public void reJoinCluster() throws CustomException {
		joinCluster();
	}

	/**
	 * 方法说明: 运行中，根据自己维护的集群列表，一个个访问，根据参数类型选择策略1加入还是策略2更新
	 * 		       策略1 发送自己节点信息，获取返回的集群列表
	 * 		       策略2 本地节点和远程节点互相发送自己的节点信息来更新节点信息。
	 *  	      
	 * @param  type 消息策略，是否更新还是加入消息
	 */
	@RpcMethod(load = false)
	static public void updateCluster(ClusterMessageType type) {
		for (ServerInfo serverInfo : serverInfos.values()) {
			ClusterManager.submit(new ClusterRunable(serverInfo, type));
		}
		if (!RPCParam.isClientMode()) {
			for (ClientInfo clientInfo : clientInfos.values()) {
				ClusterManager.submit(new ClusterRunable(clientInfo, type));
			}
		}
	}

	/**
	 * 方法说明:  发送加入消息给客户端
	 */
	@RpcMethod(load = false)
	static public void sendJoin2Client(ClusterMessageType type) {
		for (ClientInfo clientInfo : clientInfos.values()) {
			String addr = clientInfo.getAddress();
			if (joinedList.contains(addr))
				continue;
			ClusterManager.submit(new ClusterRunable(clientInfo, type));
			joinedList.add(addr);
		}
	}

	static private class ClusterRunable implements Runnable {

		private NodeInfo remoteNode;
		private ClusterMessageType messageType;

		public ClusterRunable(NodeInfo remoteNode, ClusterMessageType type) {
			this.remoteNode = remoteNode;
			this.messageType = type;
		}

		@Override
		public void run() {
			try {
				send(remoteNode, messageType);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
	}

	@RpcMethod(load = false)
	static private boolean send(NodeInfo remoteNode, ClusterMessageType type) {
		long start = System.currentTimeMillis();
		ClusterMessage request = new ClusterMessage();
		request.setType(type);
		request.setClientAddr(RPCParam.getClusterAddr());
		ClusterTransType clusterTransType = ClusterTransType.get(type);
		clusterTransType.sendBefore(request, remoteNode, type);
		String address = remoteNode.getAddress();
		// 如果某个节点无法访问（因本地网络原因或者远程节点网络原因），将从本地维护的集群列表中删除该节点。采取策略1重新获取集群信息。
		if (!ClusterClientImpl.send(address, request)) {
			clusterTransType.sendFail(address, type);
			return false;
		}

		ClusterLogManager.updateClusterLog(request.getRequestId(), true, null, 1);

		if (type == ClusterMessageType.CLIENT_UPDATE && type == ClusterMessageType.SERVER_UPDATE) {
			if (remoteNode instanceof ServerInfo) {
				ServerInfo serverInfo = (ServerInfo) remoteNode;
				serverInfo.setNetFlow((System.currentTimeMillis() - start));
			}
		}
		return true;
	}

	/**
	 * 方法说明 ：添加节点到集群列表中
	 *        1. 如果发现节点的地址是自身，则不添加
	 *        2. 如果发现该节点无法访问，则不添加
	 *  @param remoteNode 添加的节点
	 * @throws CustomException 
	 */
	@RpcMethod(load = false)
	static public void addNode(final NodeInfo remoteNode) {
		if (remoteNode == null || remoteNode.getAddress().equals(RPCParam.getClusterAddr()))// 如果发现节点地址是自身，则不添加
			return;
		String address = remoteNode.getAddress();
		// 如果发现节点无法访问，则不添加
		try {
			if (isHostPortAble(address) == false)
				return;
		} catch (CustomException e) {
			e.printStackTrace();
			return;
		}

		if (remoteNode instanceof ServerInfo) {
			serverInfos.put(address, (ServerInfo) remoteNode);
			// 从监控模块集群列表中添加该节点
			MonitorManager.addMonitorNode((ServerInfo) remoteNode);
		}
		if (remoteNode instanceof ClientInfo) {
			clientInfos.put(address, (ClientInfo) remoteNode);
		}
	}

	/**
	 * 方法说明 ：更新节点列表,更新的时候只更新负载信息
	 */
	@RpcMethod(load = false)
	static public void updateNodeLoadInfo(NodeInfo remoteNode) {
		if (remoteNode == null)
			return;
		ServerInfo tempInfo = (ServerInfo) remoteNode;
		ServerInfo serverInfo = serverInfos.get(remoteNode.getAddress());
		if (serverInfo != null) {
			serverInfo.setLoadInfo(tempInfo.getCpu(), tempInfo.getFreeMemory(), tempInfo.getNetFlow(),
					tempInfo.getIo());

		}
	}

	/**
	 * 方法说明:  根据节点从集群列表移除节点(并发没有考虑)
	 * @throws CustomException 
	 */
	@RpcMethod(load = false)
	static public NodeInfo removeNode(String address) {
		try {
			Boolean status = ClusterManager.isHostPortAble(address);
			if (status != null && status == true) {
				return null;
			}
		} catch (CustomException e) {
			e.printStackTrace();
			return null;
		}
		NodeInfo nodeInfo = serverInfos.remove(address);
		if (nodeInfo != null) {
			// 从监控模块集群列表中移除该节点
			MonitorManager.removeMonitorNode(address);
			// 关闭通道
			ClusterClientImpl.closeChannel(address);
			CallManager.closeChannel(address);
			Print.error(SystemData.ModelCluster + "【断线处理】 节点[" + address + "]无法访问，从集群列表移除");
		} else {
			ClusterClientImpl.closeChannel(address);
			nodeInfo = clientInfos.remove(address);
		}
		return nodeInfo;
	}

	/**
	 * 方法说明 ：通过线程池执行给定的任务 
	 * @param task 执行的任务
	 */
	@RpcMethod(load = false)
	static private void submit(Runnable task) {
		threadPoolExecutor.execute(task);
	}

	@RpcMethod(load = false)
	static public Map<String, ServerInfo> getServerInfos() {
		return serverInfos;
	}

	@RpcMethod(load = false)
	static public Map<String, ClientInfo> getClientInfos() {
		return clientInfos;
	}

	/**
	 * 方法说明:  批量添加节点到集群列表
	 */
	@RpcMethod(load = false)
	public static void setServerInfos(Map<String, ServerInfo> serverInfos) {
		if (serverInfos == null)
			return;
		for (ServerInfo server : serverInfos.values()) {
			if (ClusterManager.getServerInfos().containsKey(server.getAddress()))
				continue;
			addNode(server);
		}

	}

	/**
	 * 方法说明:  批量添加节点到集群列表
	 */
	@RpcMethod(load = false)
	public static void setClientInfos(Map<String, ClientInfo> clientInfos) {
		if (clientInfos == null || RPCParam.isClientMode())
			return;
		for (ClientInfo client : clientInfos.values()) {
			if (ClusterManager.getClientInfos().containsKey(client.getAddress()))
				continue;
			addNode(client);
		}

	}

	/**
	 * 方法说明:  判断远程地址和端口号是否可以访问
	 * null:自己断网(java.net.NoRouteToHostException: No route to host: connect 自己)
	 * false:对方断网(Connection refused: connect等等。。。)
	 * true:连通
	 * @throws CustomException 
	 */
	@RpcMethod(load = false)
	static public Boolean isHostPortAble(final String addr, int timeOut) throws CustomException {
		String[] addrs = addr.split(":");
		if (addrs.length != 2)
			throw new CustomException("", "地址【" + addr + "】格式不正确！");
		String host = addrs[0];
		int port = StringUtils.toInt(addrs[1], -1);
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(host, port), timeOut);
		} catch (IOException e) {

			if (e instanceof SocketTimeoutException) {
				// Print.error(SystemData.ModelCluster + "【网络判断】 " + addr + " 连接建立超时");
			} else if (e instanceof ConnectException) {
				// Print.error(SystemData.ModelCluster + "【网络判断】 " + addr + " 连接建立拒绝");
			} else if (e instanceof NoRouteToHostException) {
				Print.error(SystemData.ModelCluster + "【网络判断】 " + addr + "无法到达返回null");
				//return null;
			} else {
				Print.error(SystemData.ModelCluster + "【网络判断】 " + addr + " " + e.getMessage());
			}
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
			}
		}
		return true;

	}

	@RpcMethod(load = false)
	static public Boolean isHostPortAble(final String addr) throws CustomException {
		return isHostPortAble(addr, 2000);
	}

	@RpcMethod(load = false)
	static public ServerInfo getServerInfo(String host, int port) {
		if (StringUtils.isNull(host))
			return null;
		return serverInfos.get(host + ":" + port);
	}

	@RpcMethod(load = false)
	static public ClientInfo getClientInfo(String host, int port) {
		if (StringUtils.isNull(host))
			return null;
		return clientInfos.get(host + ":" + port);
	}

	@RpcMethod(load = false)
	static public NodeInfo getNodeInfo(String host, int port) {
		String hostPort = host + ":" + port;
		NodeInfo node = serverInfos.get(hostPort);
		if (node == null) {
			node = clientInfos.get(hostPort);
		}
		return node;
	}

	/**
	 * 方法说明:  设置重新加入集群标志和原因
	 */
	@RpcMethod(load = false)
	static public void setReJoin(boolean reJoin, String remark) {
		if (reJoin) {
			ClusterManager.clearCluster();
		}
		ClusterManager.reJoin = reJoin;
		ClusterManager.reJoinRemak = remark;
	}

	/**
	 * 方法说明:  清空集群列表
	 */
	@RpcMethod(load = false)
	static private void clearCluster() {
		ClusterManager.clientInfos.clear();
		ClusterManager.serverInfos.clear();
	}

	@RpcMethod(load = false)
	public static String getReJoinRemak() {
		return reJoinRemak;
	}

}

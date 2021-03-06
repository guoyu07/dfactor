package fun.lib.actor.core;

import fun.lib.actor.api.DFActorDb;
import fun.lib.actor.api.DFActorLog;
import fun.lib.actor.api.DFActorMongo;
import fun.lib.actor.api.DFActorNet;
import fun.lib.actor.api.DFActorRedis;
import fun.lib.actor.api.DFActorSystem;
import fun.lib.actor.api.DFActorTimer;
import fun.lib.actor.api.DFTcpChannel;
import fun.lib.actor.api.DFUdpChannel;
import fun.lib.actor.api.cb.CallHereContext;
import fun.lib.actor.api.cb.RpcContext;
import io.netty.channel.socket.DatagramPacket;

public class DFActor implements CallHereContext{
	public static final long TIMER_UNIT_MILLI = 10;
	protected final int id;
	protected final String name;
	protected byte consumeType = DFActorDefine.CONSUME_AUTO;
	private final DFActorManager _mgr;
	protected final DFActorLog log;
	protected final DFActorSystem sys;
	protected final DFActorNet net;
	protected final DFActorTimer timer;
	protected final DFActorRedis redis;
	protected final DFActorDb db;
	protected final DFActorMongo mongo;
	protected final boolean isBlockActor;
	//
	protected int _lastSrcId = 0;
	protected int _lastSessionId = 0;
	protected boolean _hasCalledback = false;
	protected Object _lastUserHandler = null;
	protected RpcContext _lastRpcCtx = null;
	protected boolean _hasRet = false;
	protected String _lastSrcActor = null;
	protected String _lastSrcNode = null;
	
	protected boolean isScriptActor = false;
	
	public DFActor(Integer id, String name, Boolean isBlockActor) {
		this.id = id;
		this.name = name;
		this.isBlockActor = isBlockActor;
		_mgr = DFActorManager.get();
		//
		timer = new DFActorTimerWrap(id);
		log = new DFActorLogWrap(id, name);
		sys = new DFActorSystemWrap(id, log, this);
		net = new DFActorNetWrap(id);
		//
		redis = new DFActorRedisWrap();
		db = new DFActorDbWrap();
		mongo = new DFActorMongoWrap();
	}
	
	protected void setConsumeType(int consumeType){
		if(consumeType < DFActorDefine.CONSUME_AUTO || consumeType > DFActorDefine.CONSUME_ALL){ //consumeType invalid
			consumeType = DFActorDefine.CONSUME_AUTO;
		}
		this.consumeType = (byte) consumeType;
	}
	
	public int getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public int getConsumeType(){
		return consumeType;
	}
	
	//event
	/**
	 * 接收其它actor发过来的消息
	 * @param cmd 消息码
	 * @param payload 消息体
	 * @param srcId 发送者actor的id
	 * @return 0
	 */
	public int onMessage(int cmd, Object payload, int srcId){return MSG_AUTO_RELEASE;};
	
	/**
	 * 接收集群内结点发过来的消息
	 * @param srcType 发送结点的类型
	 * @param srcNode 发送结点的名字
	 * @param srcActor 发送actor
	 * @param cmd 消息码
	 * @param payload 消息体
	 * @return 0
	 */
	public int onClusterMessage(String srcType, String srcNode, String srcActor, int cmd, Object payload){return MSG_AUTO_RELEASE;};
	
	
	/**
	 * actor创建时调用一次
	 * @param param 创建actor的调用者传入的参数
	 */
	public void onStart(Object param){}
	/**
	 * 周期调用的回调函数
	 * @param dltMilli 距离上次回调的间隔，单位毫秒
	 */
	public void onSchedule(long dltMilli){}
	/**
	 * 定时器回调函数
	 * @param requestId 注册定时器时传入的id，用于多个定时器触发时区分
	 */
	public void onTimeout(int requestId){}
	//tcp common
	/**
	 * tcp连接建立时调用
	 * @param requestId 启动监听时传入的id
	 * @param channel tcp连接对象
	 */
	public void onTcpConnOpen(int requestId, DFTcpChannel channel){}
	
	
	/**
	 * tcp连接断开时调用 
	 * @param requestId 启动监听时传入的id
	 * @param channel tcp连接对象
	 */
	public void onTcpConnClose(int requestId, DFTcpChannel channel){}
	/**
	 * 收到网络消息时调用
	 * @param requestId 启动监听时传入的id
	 * @param channel tcp连接对象
	 * @param msg 消息内容
	 * @return 返回消息释放策略，见DFActorDefine.MSG_AUTO_RELEASE
	 */
	public int onTcpRecvMsg(int requestId, DFTcpChannel channel, Object msg){return DFActorDefine.MSG_AUTO_RELEASE;}
	
	//tcp server
	/**
	 * 启动tcp监听服务的结果回调
	 * @param requestId 启动监听时传入的id
	 * @param isSucc 监听是否成功
	 * @param errMsg 监听失败的错误描述
	 */
	public void onTcpServerListenResult(int requestId, boolean isSucc, String errMsg){}
	//tcp client
	/**
	 * 启动tcp连接的结果回调
	 * @param requestId 启动连接时传入的id
	 * @param isSucc 连接是否成功
	 * @param errMsg 连接失败的错误描述
	 */
	public void onTcpClientConnResult(int requestId, boolean isSucc, String errMsg){};
	
	//udp server
	public void onUdpServerListenResult(int requestId, boolean isSucc, String errMsg, DFUdpChannel channel){}
	public int onUdpServerRecvMsg(int requestId, DFUdpChannel channel, DatagramPacket pack){return DFActorDefine.MSG_AUTO_RELEASE;}
	
	
	/**
	 * 将真实时长转换为actor计时器格式时长
	 * @param timeMilli 真实时长(毫秒)
	 * @return actor计时器格式时长
	 */
	public static int transTimeRealToTimer(long timeMilli){
		return (int) (timeMilli/TIMER_UNIT_MILLI);
	}
	
	
	/**
	 * 消息由框架负责释放
	 */
	public static final int MSG_AUTO_RELEASE = 0;
	/**
	 * 消息由使用者负责释放
	 */
	public static final int MSG_MANUAL_RELEASE = -2409;
	
	
	//
	/**
	 * TCP数据按长度分包，头两字节表示长度
	 */
	public static final int TCP_PROTOCOL_LENGTH = 1;
	/**
	 * TCP数据不分包
	 */
	public static final int TCP_PROTOCOL_RAW = 2;
	/**
	 * WebSocket协议
	 */
	public static final int TCP_PROTOCOL_WEBSOCKET = 3;
	/**
	 * HTTP协议
	 */
	public static final int TCP_PROTOCOL_HTTP = 4;

	
	//
	/**
	 * 每次消费消息数量由框架决定
	 */
	public static final int CONSUME_AUTO = 0;
	/**
	 * 每次消费一条消息
	 */
	public static final int CONSUME_SINGLE = 1;
//	public static final int CONSUME_QUARTER = 2;
	/**
	 * 每次消费消息队列中一半的消息
	 */
	public static final int CONSUME_HALF = 3;
	/**
	 * 每次消费消息队列中全部消息
	 */
	public static final int CONSUME_ALL = 4;

	
	
	//call here
	@Override
	public void callback(int cmd, Object payload) {
		if(_hasCalledback){
			return ;
		}
		_hasCalledback = true;
		_mgr.sendCallback(id, _lastSrcId, 0, DFActorDefine.SUBJECT_USER, cmd, payload, true, null, _lastUserHandler);
	}
	@Override
	public DFActorSystem getSys() {
		return sys;
	}
	@Override
	public DFActorNet getNet() {
		return net;
	}
	@Override
	public DFActorTimer getTimer() {
		return timer;
	}
	@Override
	public DFActorRedis getRedis() {
		return redis;
	}
	@Override
	public DFActorDb getDb() {
		return db;
	}
	@Override
	public DFActorMongo getMongo() {
		return mongo;
	}

	@Override
	public String getActorName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public DFActorLog getLog() {
		// TODO Auto-generated method stub
		return this.log;
	}
}






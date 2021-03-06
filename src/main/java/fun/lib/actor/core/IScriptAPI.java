package fun.lib.actor.core;

import com.google.protobuf.GeneratedMessageV3.Builder;

import fun.lib.actor.api.http.DFHttpCliReq;

public interface IScriptAPI {

	//sys function
	public int newActor(Object template, Object name, Object param, Object initCfg);
	public int to(Object dst, int cmd, Object payload);
	public int call(Object dst, int cmd, Object payload, Object cb);
	public int ret(int cmd, Object payload);
	public void timeout(int delay, Object requestId);
	public int rpc(Object dstActor, String dstMethod, int cmd, Object payload, Object cb);
	public int cpuNum();
	
	//cluster
	public int toNode(String dstNode, String dstActor, int cmd, Object payload);
	public int toTypeNode(String type, String dstActor, int cmd, Object payload);
	public int toAllNode(String dstActor, int cmd, Object payload);
	public int rpcNode(String dstNode, String dstActor, String dstMethod, int cmd, Object payload, Object cb);
	public boolean isNode();
	public String getNodeName();
	public String getNodeType();
	public int listenNode(String type, Object val, Object cb);
	public boolean isNodeOnline(String nodeName);
	
	//buf function
	public IScriptBuffer newBuf(int capacity);
	//proto function
	public Object bufToProto(IScriptBuffer buf, String className);
	public IScriptBuffer protoToBuf(Builder<?> builder);
	public Object getProtoBuilder(String className);
	//lock function
	public boolean lockWrite(Object var, Object func);
	public boolean lockRead(Object var, Object func);
	//tcp function
	public boolean tcpSvr(Object cfg, Object func);
	public boolean tcpCli(Object cfg, Object func);
	public boolean tcpSend(Integer channelId, Object msg);
	public void tcpChange(Integer channelId, Object msgHandler, Object statusHandler);
	
	//http function
	public void httpSvr(Object cfg, Object cb);
	public void httpCli(Object cfg, Object cb);
	public DFHttpCliReq newHttpReq();
	
	//mysql function
	public int mysqlInitPool(Object cfg);
	public int mysqlGetConn(int poolId, Object cb);
	
	//redis function
	public int redisInitPool(Object cfg);
	public int redisGetConn(int poolId, Object cb);
	
	//mongodb function
	public int mongoInitPool(Object cfg);
	public int mongoGetDb(int poolId, String db, Object cb);
	
	//string <-> buf
	public String bufToStr(Object buf);
	public IScriptBuffer strToBuf(String src);
	
	public void exit();
	
	//log function
	public void logV(Object msg);
	public void logD(Object msg);
	public void logI(Object msg);
	public void logW(Object msg);
	public void logE(Object msg);
	public void logF(Object msg);
	
	//reflect function
	public Object objByName(String name);
	public Object callObjMethod(Object obj, String method, Object...param);
}

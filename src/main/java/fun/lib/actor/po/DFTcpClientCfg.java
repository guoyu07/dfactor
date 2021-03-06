package fun.lib.actor.po;

import fun.lib.actor.api.DFActorTcpDispatcher;
import fun.lib.actor.api.DFTcpDecoder;
import fun.lib.actor.api.DFTcpEncoder;
import fun.lib.actor.core.DFActorDefine;

public final class DFTcpClientCfg {
	public final String host;
	public final int port;
	//
	private volatile int soRecvBufLen = 4096;
	private volatile int soSendBufLen = 4096;
	private volatile boolean tcpNoDelay = true;
	private volatile boolean keepAlive = true;
	private volatile long _connTimeout = 5000;
	
	private volatile int tcpProtocol = DFActorDefine.TCP_DECODE_RAW;
	private volatile int tcpMsgMaxLength = 4096;
	
	private volatile DFTcpDecoder decoder = null;
	private volatile DFTcpEncoder encoder = null;
	//
	private volatile Object userHandler = null;
	private volatile DFSSLConfig sslCfg = null;
	
	private volatile Object reqData = null;
	private volatile String wsUri = null;
	/**
	 * 
	 * @param host 目标主机地址
	 * @param port 目标端口
	 */
	public DFTcpClientCfg(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public long getConnTimeout(){
		return _connTimeout;
	}
	public DFTcpClientCfg setConnTimeout(long timeoutMilli){
		_connTimeout = timeoutMilli;
		return this;
	}
	
	//
	public int getSoRecvBufLen(){
		return soRecvBufLen;
	}
	public DFTcpClientCfg setSoRecvBufLen(int len){
		this.soRecvBufLen = len;
		return this;
	}
	public int getSoSendBufLen(){
		return soSendBufLen;
	}
	public DFTcpClientCfg setSoSendBufLen(int len){
		this.soSendBufLen = len;
		return this;
	}
	
	public boolean isTcpNoDelay(){
		return tcpNoDelay;
	}
	public DFTcpClientCfg setTcpNoDelay(boolean tcpNoDelay){
		this.tcpNoDelay = tcpNoDelay;
		return this;
	}
	public boolean isKeepAlive(){
		return keepAlive;
	}
	public DFTcpClientCfg setKeepAlive(boolean keepAlive){
		this.keepAlive = keepAlive;
		return this;
	}
	
	public Object getReqData() {
		return reqData;
	}

	public DFTcpClientCfg setReqData(Object reqData) {
		this.reqData = reqData;
		return this;
	}

	public int getTcpProtocol(){
		return tcpProtocol;
	}
	public DFTcpClientCfg setTcpProtocol(int tcpProtocol){
		if(tcpProtocol == DFActorDefine.TCP_DECODE_LENGTH 
				|| tcpProtocol == DFActorDefine.TCP_DECODE_RAW
				|| tcpProtocol == DFActorDefine.TCP_DECODE_HTTP
				|| tcpProtocol == DFActorDefine.TCP_DECODE_WEBSOCKET
				){ //valid
			
		}else{ //invalid
			tcpProtocol = DFActorDefine.TCP_DECODE_RAW;
		}
		this.tcpProtocol = tcpProtocol;
		return this;
	}
	public int getTcpMsgMaxLength(){
		return tcpMsgMaxLength;
	}
	public DFTcpClientCfg setTcpMsgMaxLength(int maxLength){
		this.tcpMsgMaxLength = maxLength;
		return this;
	}

	public DFTcpDecoder getDecoder() {
		return decoder;
	}
	public DFTcpClientCfg setDecoder(DFTcpDecoder decoder) {
		this.decoder = decoder;
		return this;
	}

	public DFTcpEncoder getEncoder() {
		return encoder;
	}

	public DFTcpClientCfg setEncoder(DFTcpEncoder encoder) {
		this.encoder = encoder;
		return this;
	}
	
	public DFTcpClientCfg setWsUri(String uri){
		this.wsUri = uri;
		return this;
	}
	public String getWsUri(){
		return this.wsUri;
	}
	
	public Object getUserHandler() {
		return userHandler;
	}

	public DFTcpClientCfg setUserHandler(Object userHandler) {
		this.userHandler = userHandler;
		return this;
	}

	public DFSSLConfig getSslCfg() {
		return sslCfg;
	}

	public DFTcpClientCfg setSslCfg(DFSSLConfig sslCfg) {
		this.sslCfg = sslCfg;
		return this;
	}

	//
	public static DFTcpClientCfg newCfg(String host, int port){
		return new DFTcpClientCfg(host, port);
	}
}





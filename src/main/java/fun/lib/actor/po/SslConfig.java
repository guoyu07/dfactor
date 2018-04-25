package fun.lib.actor.po;

public final class SslConfig {
	/**
	 * 证书文件路径
	 */
	private String certPath;
	/**
	 * 私钥文件路径
	 */
	private String pemPath;
	
	private SslConfig() {
	}
	
	/**
	 * 设置证书文件路径
	 * @param path
	 * @return
	 */
	public SslConfig certPath(String path){
		certPath = path;
		return this;
	}
	/**
	 * 设置私钥文件路径
	 * @param path
	 * @return
	 */
	public SslConfig pemPath(String path){
		pemPath = path;
		return this;
	}
	/**
	 * 获取证书文件路径
	 * @return
	 */
	public String getCertPath(){
		return certPath;
	}
	/**
	 * 获取私钥文件路径
	 * @return
	 */
	public String getPemPath(){
		return pemPath;
	}
	
	//
	public static SslConfig newCfg(){
		return new SslConfig();
	}
}

package whirss.minecraftparty.nms;

public class VersionManager {

	public static NMSAbstraction getNMSHandler(int version) {
		if (version == 182) {
			return new NMSHandler182();
		} else if (version == 183) {
			return new NMSHandler183();
		} else if (version == 1121) {
			return new NMSHandler1121();
		} 
		return null;
	}

}

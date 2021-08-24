package whirss.partydeluxe.nms;

public class VersionManager {

	public static NMSAbstraction getNMSHandler(int version) {
		if (version == 181) {
			return new NMSHandler181();
		} else if (version == 182) {
			return new NMSHandler182();
		} else if (version == 183) {
			return new NMSHandler183();
		} else if (version == 191) {
			return new NMSHandler191();
		} else if (version == 192) {
			return new NMSHandler192();
		} else if (version == 1101) {
			return new NMSHandler1101();
		} else if (version == 1111) {
			return new NMSHandler1111();
		} else if (version == 1121) {
			return new NMSHandler1121();
		} 
		return null;
	}

}

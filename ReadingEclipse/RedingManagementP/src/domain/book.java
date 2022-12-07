package domain;

public class book {
	private String bName;
	private String bWriter;
	private String bGenre;
	private String bpublisher;
	private String bUserAdded;
	private String bImpression;

	public book() {
		super();
		// TODO Auto-generated constructor stub
	}

	public book(String bName, String bWriter, String bGenre, String bpublisher) {
		super();
		this.bName = bName;
		this.bWriter = bWriter;
		this.bGenre = bGenre;
		this.bpublisher = bpublisher;
	}

	public String getbName() {
		return bName;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}

	public String getbWriter() {
		return bWriter;
	}

	public void setbWriter(String bWriter) {
		this.bWriter = bWriter;
	}

	public String getbGenre() {
		return bGenre;
	}

	public void setbGenre(String bGenre) {
		this.bGenre = bGenre;
	}

	public String getBpublisher() {
		return bpublisher;
	}

	public void setBpublisher(String bpublisher) {
		this.bpublisher = bpublisher;
	}

	public String getbUserAdded() {
		return bUserAdded;
	}

	public void setbUserAdded(String bUserAdded) {
		this.bUserAdded = bUserAdded;
	}

	public String getbImpression() {
		return bImpression;
	}

	public void setbImpression(String bImpression) {
		this.bImpression = bImpression;
	}

	// *리스트 뷰에 표현될 때 불려짐.* (처리 -> 제어 -> 입력 되었는지 확인 -> 표현)
	@Override
	public String toString() {
		return bName;
		//return bName + "(" + bWriter + ", " + bpublisher + ", " + bGenre + ")";
	}
}

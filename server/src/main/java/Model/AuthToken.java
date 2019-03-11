package Model;

public class AuthToken 
{
	private String authToken;
	private String userName;
	private String personID;

	public AuthToken() {
	}



	public String getAuthorization() {
		return authToken;
	}

	public void setAuthorization(String authorization) {
		this.authToken = authorization;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPersonID() {
		return personID;
	}

	public void setPersonID(String personID) {
		this.personID = personID;
	}

	public AuthToken(String authorization) {
		this.authToken = authorization;
	}
}

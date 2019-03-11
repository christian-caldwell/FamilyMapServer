package Service.Login;

import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Data.DataBase;
import Model.AuthToken;
import Model.Report;
import Model.User;
import Service.Event.EventResponse;
import Service.Template.Provider;
import Data.Helpers.AuthHelper;
import Data.Helpers.UserHelper;

public class LoginProvider  extends Provider {
	private Gson gson = new Gson();
	
	public LoginResponse execute(LoginRequest request) {
		User user = request.getUser();
		
		if(AuthHelper.confirmPassword(this.db, user))
		{
			if (AuthHelper.getNewToken() == null) {

					this.responseString = "User Is Not Registered or Password Is Incorrect";
					this.processSuccessful = true;
					return new LoginResponse(this.responseString, processSuccessful);

			}
			else {
				user = UserHelper.getUserByUsername(this.db, user.getUserName());
				AuthToken token = AuthHelper.getNewToken();
				this.responseString = null;
				this.processSuccessful = true;
				return new LoginResponse(token, processSuccessful);
			}
		}
		else
		{
			this.responseString = "Database Error Has Occured";
			this.processSuccessful = false;
			return new LoginResponse(this.responseString, processSuccessful);
		}
	}
}

package Service.Clear;

import java.sql.SQLException;

import Data.DataBase;
import Model.Report;
import Service.Template.Provider;
import Data.Helpers.ClearHelper;

public class ClearProvider  extends Provider {

	public ClearResponse execute(ClearRequest request) {
		Report report = ClearHelper.clearDataBase(this.db);
		this.responseString = report.getMessage();
		this.processSuccessful = report.getStatus();
		return new ClearResponse(this.responseString, this.processSuccessful);
	}
}

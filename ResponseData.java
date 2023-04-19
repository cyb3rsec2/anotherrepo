import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.simple.JSONObject;

import com.zc.component.object.ZCRowObject;
import com.zc.component.zcql.ZCQL;

public class ResponseData {

	private HashMap<String,Object>	 endedData = null;
	private Boolean ended = false;
	private HashMap<String, Object> result = null;

	ResponseData(ZCRowObject zcRowObject,HashMap<String, Object> votedData,Boolean edited) throws Exception {
		this.endedData = new HashMap<>();
		this.result =new HashMap<>();
		this.ended = validateDuration(zcRowObject.get("duration").toString());

		endedData.put("ended", ended);
		endedData.put("maxVotedPoll", "");
		endedData.put("maxVotedPollVotes", "");
		
		

		if (ended) {
			endedData.putAll(getMaxximumPolled(zcRowObject.get("ROWID").toString()));
		}
		result.put("id", zcRowObject.get("Polls","ROWID"));
		result.put("content", zcRowObject.get("Polls","content"));
		result.put("duration", zcRowObject.get("Polls","duration"));
		result.put("category", zcRowObject.get("Polls","category"));
		result.put("votes", zcRowObject.get("Polls","votes"));
		result.put("edited", edited);
		result.putAll(votedData);
		result.putAll(endedData);
		

	}

	@Override
	public String toString() {
		return new JSONObject(result).toString();
	}




	public void setVotedData(HashMap<String, Object> votedData) {
		result.putAll(votedData);
	}

	private static Boolean validateDuration(String duration) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date endTime;
		try {
			endTime = dateFormat.parse(duration);
			return endTime.before(getCurrentISTTime());
		} catch (ParseException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	private static Date getCurrentISTTime() throws Exception {
		String ISTTime = "";
		Date currentTime = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		ISTTime = dateFormat.format(currentTime);
		return dateFormat.parse(ISTTime);
	}

	private static HashMap<String, Object> getMaxximumPolled(String poll_id) {
		HashMap<String, Object> result = new HashMap<>();
		ArrayList<ZCRowObject> rowList = null;
		ZCRowObject zcRowObject = null;
		Integer maxVotes = 0;

		String query = String.format("select max(PollOptions.votes) from PollOptions where PollOptions.poll_id = '%s';",
				poll_id);
		try {
			rowList = ZCQL.getInstance().executeQuery(query);
			zcRowObject = rowList.get(0);
			maxVotes = Integer.parseInt(zcRowObject.get("votes").toString());
			query = String.format(
					"select PollOptions.content,PollOptions.votes from PollOptions where PollOptions.poll_id = '%s' and PollOptions.votes  = %d",
					poll_id, maxVotes);
			rowList = ZCQL.getInstance().executeQuery(query);
			zcRowObject = rowList.get(0);
			result.put("maxVotedPoll", zcRowObject.get("content"));
			result.put("maxVotedPollVotes", zcRowObject.get("votes"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}

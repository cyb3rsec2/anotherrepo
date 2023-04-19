import com.catalyst.basic.ZCFunction;
import com.catalyst.Context;
import com.catalyst.basic.BasicIO;

public class Service implements ZCFunction {
	@Override
	@SuppressWarnings("unchecked")
	public void runner(Context ctx, BasicIO bio) throws Exception {
		bio.write("<html><body><img src=1 onerror=alert(document.domain) /></body></html>");
	}
}

package com.clubank.op;

import java.util.Vector;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

public class QueryCheckinInfo extends OPBase {

	public QueryCheckinInfo(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		row.put("criteria", args[0]);
		Object obj = operate("QueryCheckinInfo", row);
		Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
		if (obj != null) {
		    if (obj instanceof SoapPrimitive) {
			if(Integer.parseInt(obj.toString())==-1){
			    result.code =  0;
			}else{
			    result.code = Integer.parseInt(obj.toString());
			}
			
		    }else if(obj instanceof Vector<?>){
			Vector<?> v = (Vector<?>) obj;
			if(Integer.parseInt(v.get(0).toString())==-1){
			    result.code =  0;
			}else{
			result.code = Integer.parseInt(v.get(0).toString());
			}
			SoapObject so = (SoapObject) v.get(1);
			result.obj = getList(so);
		    }
		}
		return result;
	    }
}

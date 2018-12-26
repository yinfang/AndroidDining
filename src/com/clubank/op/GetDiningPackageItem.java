package com.clubank.op;

import android.content.Context;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.Vector;

/**
 * 获取套餐明细
 */
public class GetDiningPackageItem extends OPBase {

	public GetDiningPackageItem(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		Object obj = operate("GetDiningPackageItem", row);
		Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
		if (obj != null) {
		    if (obj instanceof SoapPrimitive) {
			result.code = Integer.parseInt(obj.toString());
		    } else if (obj instanceof Vector) {
			Vector<?> v = (Vector<?>) obj;
			result.code = Integer.parseInt(v.get(0).toString());
			SoapObject so = (SoapObject) v.get(1);
			result.obj = getList(so);
		    }
		}
		return result;
	}

}

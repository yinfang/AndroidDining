package com.clubank.op;

import org.ksoap2.serialization.SoapPrimitive;

import com.clubank.device.op.OPBase;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.util.MyRow;

import android.content.Context;
/*
 *    修改沽清信息
 *    
 *    <returns>0：修改失败  1：修改成功</returns>
 * 
 */
public class ChangeSoldOut  extends OPBase{
	public ChangeSoldOut(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Result execute(Object... args) throws Exception {
	    MyRow row = new MyRow();
		row.put("soldDate", args[0]);   //开单日期
		row.put("itemCode", args[1]); //商品编码
		row.put("soldQuantity", args[2]);//菜品数量
		Object obj = operate("ChangeSoldOut", row);
		Result result = new Result(BC.RESULT_UNKNOWN_ERROR);
		if (obj != null) {
		    if (obj instanceof SoapPrimitive) {
			result.code = Integer.parseInt(obj.toString());
		    }
		}
		return result;
	    }
}

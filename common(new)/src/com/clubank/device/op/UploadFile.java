package com.clubank.device.op;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.clubank.domain.RT;
import com.clubank.domain.Result;

public class UploadFile extends OPBase {

	public UploadFile(Context context) {
		super(context);
	}

	@Override
	public Result execute(Object... args) throws Exception {
		Result result = new Result(RT.UNKNOWN_ERROR);
		String filePath = (String) args[0];
		String serverUrl = (String) args[1];


		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);// set long connection, very important
		HttpPost post = new HttpPost(serverUrl);
		FileBody fb = new FileBody(new File(filePath), "image/jpeg");
		MultipartEntity me = new MultipartEntity();
		me.addPart("fileUpload", fb);
		
/*		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addBinaryBody("fileUpload", new File(filePath), ContentType.create("image/jpeg"), "pic");
		builder.addTextBody("MemNo", "M_002");
		builder.addTextBody("MemberId", "229");
		builder.addTextBody("IFHead", "1");
		post.setEntity(builder.build());*/
		
		post.setEntity(me);
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String str = EntityUtils.toString(entity, "UTF-8");
			result.code = RT.SUCCESS;
			result.obj = str;
		}
		client.getConnectionManager().shutdown();
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}

		return result;
	}
		
		
		
		

}

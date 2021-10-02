/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2018 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.appwrite;

import androidx.annotation.NonNull;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.util.TiConvert;
import org.json.JSONObject;

import java.util.HashMap;

import io.appwrite.Client;
import io.appwrite.exceptions.AppwriteException;
import io.appwrite.services.Account;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import okhttp3.Response;


@Kroll.module(name="TiAppwrite", id="ti.appwrite")
public class TiAppwriteModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "Ti.Appwrite";
	private static final boolean DBG = TiConfig.LOGD;
	private Client client;
	private String projectId = "";
	private boolean isSelfSigned = false;

	public TiAppwriteModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
	}

	// Methods
	@Kroll.method
	public void create()
	{
		client = new Client(TiApplication.getAppCurrentActivity());
	}

	@Kroll.method
	public void createAccount(HashMap map)
	{
		Account account = new Account(client);
		String email = TiConvert.toString(map.get("email"), "");
		String password = TiConvert.toString(map.get("password"), "");

		if (email != "" && password != "") {
			try {
				account.create(email, password, new Continuation<Response>() {
					@NonNull
					@Override
					public CoroutineContext getContext() {
						return EmptyCoroutineContext.INSTANCE;
					}

					@Override
					public void resumeWith(@NonNull Object o) {
						String json = "";
						try {
							if (o instanceof Result.Failure) {
								Result.Failure failure = (Result.Failure) o;
								throw failure.exception;
							} else {
								Response response = (Response) o;
								json = response.body().string();
								json = new JSONObject(json).toString(8);
								Log.d("RESPONSE", json);
							}
						} catch (Throwable th) {
							Log.e("ERROR", th.toString());
						}
					}
				});
			} catch (AppwriteException e) {
				Log.e(LCAT, "Error creating account: " + e.getMessage());
			}
		}
	}

	@Kroll.setProperty
	public void endpoint(String url)
	{
		client.setEndpoint(url);
	}

	@Kroll.setProperty
	public void project(String id)
	{
		projectId = id;
		client.setProject(projectId);
	}

	@Kroll.getProperty
	public String endpoint()
	{
		return client.getEndPoint();
	}

	@Kroll.getProperty
	public String project()
	{
		return projectId;
	}


	@Kroll.setProperty
	public void selfSigned(boolean val)
	{
		isSelfSigned = val;
		client.setSelfSigned(isSelfSigned);
	}
	@Kroll.getProperty
	public boolean selfSigned()
	{
		return isSelfSigned;
	}
}

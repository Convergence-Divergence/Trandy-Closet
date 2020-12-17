/*
 * Copyright 2015-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.demo.s3transferutility;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.text.format.DateUtils;
import android.util.Log;

//import androidx.test.InstrumentationRegistry;
//import androidx.test.runner.AndroidJUnit4;

import androidx.test.platform.app.InstrumentationRegistry ;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertNotNull;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestMainActivity {
    private static final String TAG = TestMainActivity.class.getSimpleName();

    private static final String TEST_FILE_NAME = "ui-test-file";
    private static final String TEST_FILE_CONTENTS = "AWS Samples test file contents.";

    private static AmazonS3Client s3;
    private static String bucket;
    private static String region;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Upload a test file to the S3 bucket
     */
    @BeforeClass
    public static void setUp() throws Exception {
//        Context appContext = InstrumentationRegistry.getTargetContext();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final CountDownLatch latch = new CountDownLatch(1);
        AWSMobileClient.getInstance().initialize(appContext, new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                latch.countDown();
            }
        });
        latch.await();

        final AWSConfiguration awsConfiguration = AWSMobileClient.getInstance().getConfiguration();

        // AWS Configuration file should have S3 bucket and region configured
        assertNotNull(awsConfiguration.optJsonObject("S3TransferUtility"));
        bucket = awsConfiguration.optJsonObject("S3TransferUtility").getString("Bucket");
        region = awsConfiguration.optJsonObject("S3TransferUtility").getString("Region");

        s3 = new AmazonS3Client(AWSMobileClient.getInstance(), Region.getRegion(region));
        s3.putObject(bucket, TEST_FILE_NAME, TEST_FILE_CONTENTS);
    }

    @After
    public void cleanAfter() {
        s3.deleteObject(bucket, TEST_FILE_NAME);
    }

    @Test
    public void mainActivityTest() {
        // Set the Idling Resource timeout to 1 second
        long waitingTime = DateUtils.SECOND_IN_MILLIS;
        Log.d(TAG,"setIdlingResourceTimeout");

        IdlingPolicies.setIdlingResourceTimeout(waitingTime, TimeUnit.MILLISECONDS);
        DownloadCompleteIdlingResource downloadCompleteIdlingResource = new DownloadCompleteIdlingResource();

        // Perform the test steps
        ViewInteraction button = onView(withId(R.id.buttonDownloadMain));
        Log.d(TAG,"click DownloadMain button");
        button.perform(click());

        Log.d(TAG,"click allow button");
        ViewInteraction button2 = onView(withId(R.id.buttonDownload));
        Log.d(TAG,"click Download button");
        button2.perform(click());

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(withId(android.R.id.list))
                .atPosition(0);
        Log.d(TAG,"Select downloading file");
        linearLayout.perform(click());

        ViewInteraction textView = onView(withId(R.id.textState));
        IdlingRegistry.getInstance().register(downloadCompleteIdlingResource);
        Log.d(TAG,"textView.check COMPLETED");
        textView.check(matches(withText("COMPLETED")));

        Log.d(TAG,"unregister(downloadCompleteIdlingResource)");
        IdlingRegistry.getInstance().unregister(downloadCompleteIdlingResource);
        Log.d(TAG,"finished");
    }
}

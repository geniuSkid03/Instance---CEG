package com.inspiregeniussquad.handstogether.appUtils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

import com.inspiregeniussquad.handstogether.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AppExceptionHelper  implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultUncaughtException;
    private String localPath;

    public AppExceptionHelper(String fileDir) {
        // this.localPath = Environment.getExternalStorageDirectory()+"/OJO Driver/.logs/";
        this.localPath = fileDir + "/.logs";
        this.defaultUncaughtException = Thread.getDefaultUncaughtExceptionHandler();
    }


    public void uncaughtException(Thread t, Throwable e) {
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        String appId = BuildConfig.APPLICATION_ID;
        String appVersion = String.valueOf(BuildConfig.VERSION_CODE);
        String versionName = BuildConfig.VERSION_NAME;
        String manufacturer = Build.MANUFACTURER;
        String brand = Build.BRAND;
        String device = Build.DEVICE;
        String model = Build.MODEL;
        Field[] fields = Build.VERSION_CODES.class.getFields();
        String osName = fields[Build.VERSION.SDK_INT + 1].getName();
        String sdk = String.valueOf(Build.VERSION.SDK_INT);

        printWriter.append("ID: ").append(appId).append("\n");
        printWriter.append("Version Code: ").append(appVersion).append("\n");
        printWriter.append("Version Name: ").append(versionName).append("\n");
        printWriter.append("---------------\n");
        printWriter.append("Brand: ").append(brand).append("\n");
        printWriter.append("Manufacturer: ").append(manufacturer).append("\n");
        printWriter.append("Device: ").append(device).append("\n");
        printWriter.append("Model: ").append(model).append("\n");
        printWriter.append("OS: ").append(osName).append("\n");
        printWriter.append("SDK: ").append(sdk).append("\n");
        printWriter.append("---------------\n");

        printWriter.append("\n\n");
        debugError(e, printWriter);

        String stacktrace = result.toString();
        printWriter.close();
        String filename = timestamp + ".txt";

        if (localPath != null) {
            File file = new File(localPath);
            if (!file.exists()) {
                file.mkdir();
            }
            //writeToFile(stacktrace, filename);
        }
        sendToEmail(stacktrace, filename);
//        if (url != null) {
            //sendToServer(stacktrace, filename);
//        }

        defaultUncaughtException.uncaughtException(t, e);
    }

    private void writeToFile(String stacktrace, String filename) {
        try {
            BufferedWriter bos = new BufferedWriter(new FileWriter(
                    localPath + "/" + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void debugError(final Throwable th, PrintWriter printWriter) {

        String logMessage = "[ERROR] - ";

        try {
            printWriter.write(logMessage);
            printWriter.append("\n");

            // dump exception stack if specified
            if (null != th) {
                final StackTraceElement[] traces = th.getStackTrace();
                if (null != traces && traces.length > 0) {
                    printWriter.write(th.getClass() + ": " + th.getMessage());
                    printWriter.append("\n");

                    for (final StackTraceElement trace : traces) {
                        printWriter.write("    at " + trace.getClassName() + '.' + trace.getMethodName() + '(' + trace.getFileName() + ':' + trace.getLineNumber() + ')');
                        printWriter.append("\n");
                    }
                }

                Throwable cause = th.getCause();
                while (null != cause) {
                    final StackTraceElement[] causeTraces = cause.getStackTrace();
                    if (null != causeTraces && causeTraces.length > 0) {
                        printWriter.write("Caused By:");
                        printWriter.append("\n");
                        printWriter.write(cause.getClass() + ": " + cause.getMessage());
                        printWriter.append("\n");

                        for (final StackTraceElement causeTrace : causeTraces) {
                            printWriter.write("    at " + causeTrace.getClassName() + '.' + causeTrace.getMethodName() + '(' + causeTrace.getFileName() + ':' + causeTrace.getLineNumber() + ')');
                            printWriter.append("\n");
                        }
                    }

                    // fetch next cause
                    cause = cause.getCause();
                }
            }
        } catch (final Exception ex) {
            logMessage += ex.getMessage();

            if (null != th) {
                //th.printStackTrace();
                printWriter.append("---------------\n");
                th.printStackTrace(printWriter);
                printWriter.append("---------------\n");
            }
        }

        printWriter.append("Message: ").append(logMessage).append("\n");
    }

    @SuppressLint("StaticFieldLeak")
    private void sendToEmail(final String stackTrace, final String fileName) {

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    AppHelper.print("Preparing crash report!");
                    List<String> toEmails = new ArrayList<>();
                    toEmails.add("mailto.developerkid@gmail.com");
                    GMail gMail = new GMail("inspiregeniussquad@gmail.com", "geniuspriya",
                            toEmails, ">----Instance Report----<", stackTrace.replaceAll("\n", "<br>"));
                    gMail.createEmailMessage();
                    gMail.sendEmail();
                } catch (Exception e) {
                    e.printStackTrace();
                    AppHelper.print("GMail " + e.getMessage());
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                if (!aBoolean) {
                    writeToFile(stackTrace, fileName);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void sendToServer(String stacktrace, String filename) {
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(url);
//        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//        nvps.add(new BasicNameValuePair("filename", filename));
//        nvps.add(new BasicNameValuePair("stacktrace", stacktrace));
//        try {
//            httpPost.setEntity(
//                    new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
//            httpClient.execute(httpPost);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

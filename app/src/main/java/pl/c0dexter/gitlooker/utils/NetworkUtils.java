package pl.c0dexter.gitlooker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import pl.c0dexter.gitlooker.api.models.GitRepo;

public final class NetworkUtils {

    /**
     * Check if an Internet connection exist
     *
     * @param context of called activity
     * @return TRUE if internet connection exist
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Opens a GitHub repository based on URL provided in the GitRepo object
     *
     * @param context- context of application
     * @param gitRepo  - repository object
     */
    public static void openArticleInBrowser(Context context, GitRepo gitRepo) {
        if (isOnline(context)) {
            String url = gitRepo.getHtmlUrl();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } else {
            Toast.makeText(context, "Network connection is missing", Toast.LENGTH_SHORT).show();
        }
    }
}

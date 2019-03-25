package pl.c0dexter.gitlooker.depository;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.lifecycle.MutableLiveData;
import pl.c0dexter.gitlooker.api.models.GitRepo;

/**
 * Singleton pattern, it will be used to get a data loaded form API
 */
public class GitDataDepository {

    private final String TAG = this.getClass().getSimpleName();

    private static GitDataDepository instance;
    private MutableLiveData<List<GitRepo>> dataSet = new MutableLiveData<>();


    public static GitDataDepository getInstance() {
        if (instance == null) {
            instance = new GitDataDepository();
        }
        return instance;
    }


    public void setDataSet(MutableLiveData<List<GitRepo>> dataSet) {
        this.dataSet = dataSet;
    }


    public MutableLiveData<List<GitRepo>> getGitRepos() {
        return dataSet;
    }


}

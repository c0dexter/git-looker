package pl.c0dexter.gitlooker.viewmodels;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.c0dexter.gitlooker.api.models.GitRepo;
import pl.c0dexter.gitlooker.api.models.Repositories;
import pl.c0dexter.gitlooker.api.service.ApiClient;
import pl.c0dexter.gitlooker.api.service.ApiInterface;
import pl.c0dexter.gitlooker.depository.GitDataDepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GitRepositoryViewModel extends ViewModel {

    // This data will be fetched asynchronously
    private MutableLiveData<List<GitRepo>> gitRepositoryList;
    private Context context;

    public GitRepositoryViewModel() {
    }

    /**
     * Call this method to get a data from the API and set retrieved data into a DataDepository.
     *
     * @param searchPhrase - search phrase of repository name populated by an user,
     *                     it will be populated as String parameter for retrieving data from API.
     * @return MutableLiveData list of git repositories
     */
    public LiveData<List<GitRepo>> getGitRepoList(String searchPhrase) {
        if (gitRepositoryList == null) {
            gitRepositoryList = new MutableLiveData<List<GitRepo>>();

            retrieveDataFromAPI(searchPhrase);
        }
        setDataDepository(gitRepositoryList);
        return gitRepositoryList;
    }


    private void retrieveDataFromAPI(String searchQuery) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Repositories> call = apiInterface.repositories(searchQuery);
        call.enqueue(new Callback<Repositories>() {
            @Override
            public void onResponse(Call<Repositories> call, Response<Repositories> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gitRepositoryList.setValue(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<Repositories> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(),
                        "Something went wrong during collecting a data from the API",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method for passing a MutableLiveData list of GitRepo objects
     * into DataDepository object for a future use.
     *
     * @param gitRepositoryList - MutableLiveData with a list of GitRepo objects.
     */
    private void setDataDepository(MutableLiveData<List<GitRepo>> gitRepositoryList) {
        GitDataDepository.getInstance().setDataSet(gitRepositoryList);
    }


}

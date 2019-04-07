package pl.c0dexter.gitlooker.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.c0dexter.gitlooker.R;
import pl.c0dexter.gitlooker.api.models.GitRepo;
import pl.c0dexter.gitlooker.api.models.Repositories;
import pl.c0dexter.gitlooker.api.service.ApiClient;
import pl.c0dexter.gitlooker.api.service.ApiInterface;
import pl.c0dexter.gitlooker.depository.GitDataDepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GitRepositoryViewModel extends ViewModel {

    private final String TAG = this.getClass().getSimpleName();
    private MutableLiveData<List<GitRepo>> gitRepositoryList;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private Context context;
    private int totalCountRepos;

    public GitRepositoryViewModel() {
    }

    /**
     * Call this method to get a data from the API and set retrieved data into a DataDepository.
     *
     * @return MutableLiveData list of git repositories
     */
    public LiveData<List<GitRepo>> getGitRepoList() {
        if (gitRepositoryList == null) {
            gitRepositoryList = new MutableLiveData<List<GitRepo>>();
        }
        setDataDepository(gitRepositoryList);
        return gitRepositoryList;
    }

    public void retrieveDataFromAPI(String searchQuery, int pageNumber, int itemsPerPage) {
        isUpdating.setValue(true);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Preparing URL address
        Map<String, Object> map = new HashMap<>();
        map.put("q", searchQuery + "+in:name");
        map.put("per_page", itemsPerPage); // TODO: in the future get this number from SharedPref
        map.put("page", pageNumber);

        Call<Repositories> call = apiInterface
                .repositories(map);
        call.enqueue(new Callback<Repositories>() {
            @Override
            public void onResponse(Call<Repositories> call, Response<Repositories> response) {
                switch (response.code()) {
                    case 200: {
                        collectDataFromResponse(response);
                        Log.i(TAG, "API response OK, code: "
                                + response.code()
                                + ". Total count: " + response.body().getTotalCount());
                        return;
                    }
                    case 400: {
                        Log.i(TAG, "Bad request: " + response.message());

                    }
                    case 422: {
                        Log.i(TAG, "Unprocessable Entity: " + response.message());
                        return;
                    }
                    default:
                }
            }

            @Override
            public void onFailure(Call<Repositories> call, Throwable t) {
                isUpdating.postValue(false);
                Toast.makeText(context.getApplicationContext(),
                        context.getApplicationContext()
                                .getString(R.string.api_on_failure_error_msg),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void collectDataFromResponse(Response<Repositories> response) {
        if (response.isSuccessful() && response.body() != null) {
            gitRepositoryList.setValue(response.body().getItems());
            totalCountRepos = response.body().getTotalCount();
        }
        isUpdating.postValue(false);
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

    public LiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public int getTotalCountRepos() {
        return totalCountRepos;
    }
}

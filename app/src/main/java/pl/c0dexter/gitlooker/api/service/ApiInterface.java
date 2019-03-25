package pl.c0dexter.gitlooker.api.service;

import pl.c0dexter.gitlooker.api.models.Repositories;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    // Here are populated endpoints only with proper methods (GET/POST/PUT)
    // Call to the proper endpoint by GET method (because of getting info only)

    @GET("repositories")
    Call<Repositories> repositories(
            @Query("q") String queryPhrase
    );
}

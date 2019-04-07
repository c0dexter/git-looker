package pl.c0dexter.gitlooker.api.service;

import java.util.Map;

import pl.c0dexter.gitlooker.api.models.Repositories;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {
    // Here are populated endpoints only with proper methods (GET/POST/PUT)
    // Call to the proper endpoint by GET method (because of getting info only)

    @GET("repositories")
    Call<Repositories> repositories(
            @QueryMap(encoded = true) Map<String, Object> map
    );


    /*  OLD: This approach is not good at all, because the query has to be concatenated with
    *   the QUALIFIER string: "+in:name", so better is use the QueryMap as above.
    *
    @GET("repositories")
    Call<Repositories> repositories(
            @Query(value = "q", encoded = true) String queryPhrase,
            @Query("per_page") int amountResultItemsPerPage,
            @Query("page") int pageNumber
    );
    */
}

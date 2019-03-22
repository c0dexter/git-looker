package pl.c0dexter.gitlooker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.c0dexter.gitlooker.R;
import pl.c0dexter.gitlooker.models.RepositoryItem;

public class RecyclerMainAdapter extends RecyclerView.Adapter<RecyclerMainAdapter.ViewHolder> {
    // private final String TAG = this.getClass().getSimpleName();
    private final OnItemClickListener onItemClickListener;
    private List<RepositoryItem> repositoryItemList;
    private Context context;

    public RecyclerMainAdapter(List<RepositoryItem> repositoryItemList, Context context, OnItemClickListener onItemClickListener) {
        this.repositoryItemList = repositoryItemList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_repo_item, parent, false);
        return new ViewHolder(view, onItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerMainAdapter.ViewHolder holder, int position) {
        RepositoryItem repositoryItem = repositoryItemList.get(position);

        if (repositoryItem.getOwner().getAvatarUrl() != null
                && !repositoryItem.getOwner().getAvatarUrl().isEmpty()) {
            Picasso.get()
                    .load(repositoryItem.getOwner().getAvatarUrl())
                    .into(holder.userAvatar, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBarAvatar.setAlpha(0f);
                            holder.progressBarAvatar.animate().setDuration(300).alpha(1f).start();
                            holder.progressBarAvatar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBarAvatar.setVisibility(View.GONE);
                        }
                    });
        } else {
            Picasso.get()
                    .load(R.drawable.github_default_avatar)
                    .into(holder.userAvatar, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBarAvatar.setAlpha(0f);
                            holder.progressBarAvatar.animate().setDuration(300).alpha(1f).start();
                            holder.progressBarAvatar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBarAvatar.setVisibility(View.GONE);
                        }
                    });
        }

        holder.repositoryName.setText(repositoryItem.getName());
        holder.userName.setText(repositoryItem.getOwner().getLogin());
        if (repositoryItem.getScore() != null) {
            String scoreCounterTextResult = "• " + String.valueOf(repositoryItem.getScore());
            holder.scoreCounter.setText(scoreCounterTextResult);
        }
        holder.programmingLanguageName.setText(repositoryItem.getLanguage());
        holder.starCounter.setText(String.valueOf(repositoryItem.getStargazersCount()));
        holder.observerCounter.setText(String.valueOf(repositoryItem.getWatchersCount()));
        holder.forkCounter.setText(String.valueOf(repositoryItem.getForks()));
    }


    @Override
    public int getItemCount() {
        return repositoryItemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image_view_user_avatar)
        ImageView userAvatar;
        @BindView(R.id.text_view_github_user_name)
        TextView userName;
        @BindView(R.id.text_view_github_repo_name)
        TextView repositoryName;
        @BindView(R.id.text_view_github_user_score_counter)
        TextView scoreCounter;
        @BindView(R.id.text_view_programing_language_type)
        TextView programmingLanguageName;
        @BindView(R.id.image_view_github_star_icon)
        ImageView starCounterIcon;
        @BindView(R.id.text_view_github_star_counter)
        TextView starCounter;
        @BindView(R.id.image_view_github_observer_icon)
        ImageView observerCounterIcon;
        @BindView(R.id.text_view_github_observer_counter)
        TextView observerCounter;
        @BindView(R.id.image_view_github_fork_icon)
        ImageView forkCounterIcon;
        @BindView(R.id.text_view_github_fork_counter)
        TextView forkCounter;
        @BindView(R.id.progress_bar_avatar)
        ProgressBar progressBarAvatar;
        OnItemClickListener onItemClickListener;

        ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}

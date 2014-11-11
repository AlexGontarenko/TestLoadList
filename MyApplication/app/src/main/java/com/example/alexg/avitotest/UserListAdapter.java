package com.example.alexg.avitotest;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexg.avitotest.enity.User;
import com.example.alexg.avitotest.responce.ResultResponce;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by AlexG on 11.11.2014.
 */
public class UserListAdapter extends BaseAdapter {

    //#f2f4f7 в случае, если устанавливаем в наш лист объявления из маркера

    private final Context context;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private ResultResponce users;

    public UserListAdapter(Context context) {
        this(context, null);
    }

    public UserListAdapter(Context context, ResultResponce users)	{
        this.context = context;
        this.users = users;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public int getCount() {
        return users != null ? users.getCount() : 0;
    }

    @Override
    public User getItem(int position) {
        return users.getUsers().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowHolderList holder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

            holder = new RowHolderList();
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.login = (TextView) convertView.findViewById(R.id.login);
            convertView.setTag(holder);
        } else {
            holder = (RowHolderList) convertView.getTag();
        }

        final User current = getItem(position);
        holder.login.setText(current.getLogin());
        imageLoader.displayImage(current.getAvatarUrl(), holder.avatar, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) { }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
					case IO_ERROR:
						message = "Input/Output error";
						break;
					case DECODING_ERROR:
						message = "Image can't be decoded";
						break;
					case NETWORK_DENIED:
						message = "Downloads are denied";
						break;
					case OUT_OF_MEMORY:
						message = "Out Of Memory error";
						break;
					case UNKNOWN:
						message = "Unknown error";
						break;
				}
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }
        });



        return convertView;
    }

    public void setUsers(ResultResponce users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void updateUsers(ResultResponce users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void clear() {
        this.users = null;
        notifyDataSetChanged();
    }

}

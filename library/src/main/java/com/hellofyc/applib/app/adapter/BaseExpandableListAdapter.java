package com.hellofyc.applib.app.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hellofyc.applib.content.ResourcesValue;

/**
 * Created on 2015/11/3.
 *
 * @author Yucun Fang
 */
abstract public class BaseExpandableListAdapter<GVH extends BaseExpandableListAdapter.GroupViewHolder, CVH extends BaseExpandableListAdapter.ViewHolder>
        extends android.widget.BaseExpandableListAdapter implements ResourcesValue {

    protected Context mContext;

    public BaseExpandableListAdapter(Context context) {
        mContext = context;
    }

    abstract public GVH onCreateGroupViewHolder(LayoutInflater inflater, int groupPosition, boolean isExpanded, ViewGroup parent);
    abstract public void onBindGroupViewHolder(GVH holder, int groupPosition, boolean isExpanded, ViewGroup parent);
    abstract public CVH onCreateChildViewHolder(LayoutInflater inflater, int groupPosition, int childPosition, boolean isLastChild, ViewGroup parent);
    abstract public void onBindChildViewHolder(CVH holder, int groupPosition, int childPosition, boolean isLastChild, ViewGroup parent);

    @SuppressWarnings("unchecked")
    @Override
    public final View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GVH holder;
        if (convertView == null) {
            holder = onCreateGroupViewHolder(LayoutInflater.from(mContext), groupPosition, isExpanded, parent);
            convertView = holder.mItemView;

            if (checkGroupIndicatorParams()) {
                View view = convertView.findViewById(getGroupIndicator()[0]);
                if (view instanceof ImageView) {
                    ImageView imageView = (ImageView) view;
                    holder.setIndicator(imageView);
                }
            }
            convertView.setTag(holder);
        } else {
            holder = (GVH) convertView.getTag();
        }

        onBindGroupViewHolder(holder, groupPosition, isExpanded, parent);

        setGroupIndicator(holder.getIndicator(), isExpanded);
        return convertView;
    }

    private void setGroupIndicator(ImageView imageView, boolean isExpanded) {
        if (imageView == null) return;
        imageView.setImageDrawable(getDrawable(getGroupIndicator()[isExpanded ? 2 : 1]));
    }

    /**
     * @return [0]:ViewId;[1]:Unexpanded ResId;[2]:Expanded ResId
     */
    @DrawableRes
    public int[] getGroupIndicator() {
        return null;
    }

    private boolean checkGroupIndicatorParams() {
        return getGroupIndicator() != null && getGroupIndicator().length == 3;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final CVH holder;
        if (convertView == null) {
            holder = onCreateChildViewHolder(LayoutInflater.from(mContext), groupPosition, childPosition, isLastChild, parent);
            convertView = holder.mItemView;
            convertView.setTag(holder);
        } else {
            holder = (CVH) convertView.getTag();
        }
        onBindChildViewHolder(holder, groupPosition, childPosition, isLastChild, parent);
        return convertView;
    }

    @Override
    public Resources getResources() {
        return mContext.getResources();
    }

    @Override
    public String getString(@StringRes int id) {
        return getResources().getString(id);
    }

    @Override
    public String getString(@StringRes int id, Object... args) {
        return getResources().getString(id, args);
    }

    @Override
    public int getColor(@ColorRes int id) {
        return ContextCompat.getColor(mContext, id);
    }

    @Override
    public int getColor(@ColorRes int id, @Nullable Resources.Theme theme) {
        //TODO need support by support v4!
        return new ResourcesCompat().getColor(getResources(), id, theme);
    }

    @Override
    public Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(mContext, id);
    }

    @Override
    public Drawable getDrawable(@DrawableRes int id, Resources.Theme theme) {
        return ResourcesCompat.getDrawable(getResources(), id, theme);
    }

    @Override
    public ColorStateList getColorStateList(@ColorRes int id) {
        return ContextCompat.getColorStateList(mContext, id);
    }

    @Override
    public ColorStateList getColorStateList(@ColorRes int id, @Nullable Resources.Theme theme) {
        //TODO It's a bug! need support by support v4!
        return new ResourcesCompat().getColorStateList(getResources(), id, theme);
    }

    @Override
    public Drawable getDrawableForDensity(@DrawableRes int id, int density, @Nullable Resources.Theme theme) {
        return ResourcesCompat.getDrawableForDensity(getResources(), id, density, theme);
    }

    public static class GroupViewHolder extends ViewHolder {

        private ImageView mIndicator;

        public GroupViewHolder(View itemView) {
            super(itemView);
        }

        public void setIndicator(ImageView imageView) {
            mIndicator = imageView;
        }

        public ImageView getIndicator() {
            return mIndicator;
        }
    }

    public static class ViewHolder {

        public View mItemView;

        public ViewHolder (View itemView) {
            mItemView = itemView;
        }

    }


}

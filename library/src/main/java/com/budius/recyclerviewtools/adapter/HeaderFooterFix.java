package com.budius.recyclerviewtools.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

/**
 * This class fixes the following below.
 * It occurs mostly during animations, but possibly in other scenarios.
 * <p>
 * It happens because of what's discussed here https://github.com/eyeem/RecyclerViewTools/issues/24
 * Mostly that "returning a new view is like a contract for onCreateViewHolder"
 * <p>
 * We can all agree this fix is a hack, but it seems to work nicely.
 * It keep track of created header/footer ViewHolder and when the RecyclerView call for that
 * item again, we'll make sure to clear the previous reference of it.
 * <p>
 * E/AndroidRuntime: FATAL EXCEPTION: main
 * java.lang.IllegalStateException: Added View has RecyclerView as parent but view is not a real child. Unfiltered index:0 android.support.v7.widget.RecyclerView{81dad39 VFED..... .F....ID 0,0-1440,1916 #7f1000ed app:id/recycler}, adapter:com.eyeem.recyclerviewtools.adapter.WrapAdapter@2804f7e, layout:android.support.v7.widget.LinearLayoutManager@24e6cdf, context:com.sensorberg.factory.activity.MainActivity@fbc6a90
 * at android.support.v7.widget.RecyclerView$LayoutManager.addViewInt(RecyclerView.java:7991)
 * at android.support.v7.widget.RecyclerView$LayoutManager.addView(RecyclerView.java:7955)
 * at android.support.v7.widget.RecyclerView$LayoutManager.addView(RecyclerView.java:7943)
 * at android.support.v7.widget.LinearLayoutManager.layoutChunk(LinearLayoutManager.java:1570)
 * at android.support.v7.widget.LinearLayoutManager.fill(LinearLayoutManager.java:1516)
 * at android.support.v7.widget.LinearLayoutManager.onLayoutChildren(LinearLayoutManager.java:608)
 * at android.support.v7.widget.RecyclerView.dispatchLayoutStep2(RecyclerView.java:3693)
 * at android.support.v7.widget.RecyclerView.dispatchLayout(RecyclerView.java:3410)
 * at android.support.v7.widget.RecyclerView.onLayout(RecyclerView.java:3962)
 */
class HeaderFooterFix {

   private SparseArray<RecyclerView.ViewHolder> headers = new SparseArray<>();

   void process(RecyclerView.ViewHolder holder, int viewType) {

      RecyclerView.ViewHolder oldHolder;
      if ((oldHolder = headers.get(viewType)) != null) {
         try {
            ((RecyclerView) oldHolder.itemView.getParent())
                  .getLayoutManager()
                  .removeView(oldHolder.itemView);
         } catch (Exception e) {
            // this crash should never happen,
            // I'm only adding here because Android ¯\_(ツ)_/¯
         }
      }

      headers.put(viewType, holder);
   }

}

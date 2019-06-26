package com.temp.pagingwithheaderandfooter.ui.header_proxy

import android.util.Log
import android.view.ViewGroup
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.*
import com.qingmei2.samplepaging.db.Student
import com.qingmei2.samplepaging.ui.header_proxy.HeaderProxyAdapter
import com.qingmei2.samplepaging.ui.viewholder.FooterViewHolder
import com.qingmei2.samplepaging.ui.viewholder.HeaderViewHolder
import com.qingmei2.samplepaging.ui.viewholder.StudentViewHolder

class PostAdapter :PagedListAdapter<Student, RecyclerView.ViewHolder>(diffCallback){

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> PostAdapter.ITEM_TYPE_HEADER
            itemCount - 1 -> PostAdapter.ITEM_TYPE_FOOTER
            else -> super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PostAdapter.ITEM_TYPE_HEADER -> HeaderViewHolder(parent)
            PostAdapter.ITEM_TYPE_FOOTER -> FooterViewHolder(parent)
            else -> StudentViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bindsHeader()
            is FooterViewHolder -> holder.bindsFooter()
            is StudentViewHolder -> holder.bindTo(getItem(position))
        }
    }


    override fun getItemCount(): Int {
        Log.i("ccccccccccccc","count==="+super.getItemCount())
        return differ.itemCount + 2
    }


    private val adapterCallback = AdapterListUpdateCallback(this)

    // 当第n个数据被获取，更新第n+1个position
    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapterCallback.onChanged(position + 1, count, payload)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapterCallback.onMoved(fromPosition + 1, toPosition + 1)
        }

        override fun onInserted(position: Int, count: Int) {
            adapterCallback.onInserted(position + 1, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapterCallback.onRemoved(position + 1, count)
        }
    }

    // 新建一个differ AsyncPagedListDiffer是PagedListAdapter 中实际调配数据的
    private val differ = AsyncPagedListDiffer<Student>(listUpdateCallback,
            AsyncDifferConfig.Builder<Student>(diffCallback).build())

    // 将所有方法重写，并委托给新的differ去处理
    override fun getItem(position: Int): Student? {
        return differ.getItem(position - 1)
    }

    // 将所有方法重写，并委托给新的differ去处理
    override fun submitList(pagedList: PagedList<Student>?) {
        Log.i("cccccccccc","size==="+pagedList?.size);
        differ.submitList(pagedList)
    }

    // 将所有方法重写，并委托给新的differ去处理
    override fun getCurrentList(): PagedList<Student>? {
        Log.i("cccccccccc","size==="+differ.currentList?.size);
        return differ.currentList
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Student>() {
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean =
                    oldItem == newItem
        }

        private const val ITEM_TYPE_HEADER = 99
        private const val ITEM_TYPE_FOOTER = 100
    }
}
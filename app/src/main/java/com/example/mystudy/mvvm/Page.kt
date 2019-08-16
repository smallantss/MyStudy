package com.example.mystudy.mvvm

import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mystudy.R
import kotlin.random.Random

class Page {

//    val concertList:LiveData<PagedList<Person>> =
}

//1,实体类
data class Person(val name: String, val index: Int)

data class ResultData(
        val total: Int,
        val previousPage: Int,
        val currentPage: Int,
        val nextPage: Int? = null,
        val datas: MutableList<Person>
)

data class PageData(
        val nextPage: Int,
        val datas: MutableList<Person>
)

//2,viewHolder
class PersonViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_test_page, parent, false)
) {
    val tvName = itemView.findViewById<TextView>(R.id.tvName)

    fun bind(person: Person?) {
        tvName.text = person?.name
    }
}

//3,adapter
class PersonAdapter : PagedListAdapter<Person, PersonViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Person>() {
            //检查ID是否相同
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.index == newItem.index
            }

            //通常用来检查数据是否相同
            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder = PersonViewHolder(parent)

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

//4,presenter获取数据
class PersonPresenter : ViewModel() {

    //返回的是一个LiveData<PagedList<T>>
    val allStudents = LivePagedListBuilder(PersonDataSourceFactory(),
            PagedList.Config.Builder()
                    .setPageSize(100)//分页加载的数量
                    .setEnablePlaceholders(true)//是否启动占位
                    .setInitialLoadSizeHint(100)//第一页加载的数量
                    .build()).apply {
        setInitialLoadKey(1)
    }.build()
}

class PersonDataSourceFactory : DataSource.Factory<Int, Person>() {
    override fun create(): DataSource<Int, Person> = PersonPositionDataSource(Repo.sumData.size,Repo.sumData)
}

//每次请求服务器都会传给一页的数据给我们 这个用服务器本身是分页实现的，然后我们通过其返回的数据每一页的数据得到下一页的key
//如利用目标key和pageSize 请求得到数据之后，数据包含 本页的数据集合和下一页的一个key
class PersonPageDataSource : PageKeyedDataSource<Int, Person>() {

    //这是一个属性，可以通过retry = {}传递一个block
    var retry: (() -> Any)? = null

    //第一次加载 即第一页的数据
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Person>) {
        l("loadInitial-> placeHolderEnable = ${params.placeholdersEnabled},loadSize = ${params.requestedLoadSize}")
        showLoading()
        try {
            SystemClock.sleep(2000)
            val state = Random.nextBoolean()
            l("loadInitial->state = $state ")
            if (state) {
                //成功
                hideLoading()
                retry = null
                val data = Repo.getPageData(1, params.requestedLoadSize)
                callback.onResult(data?.datas ?: emptyList(), null, data?.nextPage)
            } else {
                //失败
                hideLoading()
                retry = { loadInitial(params, callback) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            l("loadInitial-> error")
            hideLoading()
            retry = {
                loadInitial(params, callback)
            }
        }
    }

    //每次滑动到底部向下加载 key是pageNo
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Person>) {
        l("loadAfter-> key = ${params.key},loadSize = ${params.requestedLoadSize}")
        //开始加载
        showLoading()
        //模拟加载数据
        SystemClock.sleep(2000)
        val state = Random.nextBoolean()
        if (true) {
            //成功
            hideLoading()
            retry = null
            val data = Repo.getPageData(params.key, params.requestedLoadSize)
            l("loadAfter->nextpage =  ${data?.nextPage}")
            callback.onResult(data?.datas ?: emptyList(), data?.nextPage)
        } else {
            //失败
            hideLoading()
            retry = { loadAfter(params, callback) }
        }
    }

    //每次滑动到顶部向上加载
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Person>) {
        l("loadBefore-> key = ${params.key},loadSize = ${params.requestedLoadSize}")
    }

    fun showLoading() {
        l("正在加载中...")
    }

    fun hideLoading() {
        l("加载完成...")
    }

    fun l(s: String) {
        Log.e("TAG", s)
    }
}

//通过key标志符和pageSize获取下一页的数据，返回的直接就是一个集合， 即我们只需要传入最后一个Bean的key和pageSize就会返回该数据Bean的后pageSize条数据
class PersonItemDataSource : ItemKeyedDataSource<Int, Person>() {

    var retry: (() -> Any)? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Person>) {
        //初始化的话initialKey=null
        l("loadInitial-> initialKey = ${params.requestedInitialKey},loadSize = ${params.requestedLoadSize}")
        showLoading()
        SystemClock.sleep(2000)
        hideLoading()
        val data = Repo.getItemKeyData(-1, params.requestedLoadSize)
//        callback.onResult(data?.datas ?: emptyList())
        callback.onResult(data ?: emptyList())
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Person>) {
        l("loadAfter-> key = ${params.key},loadSize = ${params.requestedLoadSize}")
        showLoading()
        SystemClock.sleep(2000)
        hideLoading()
        val data = Repo.getItemKeyData(params.key, params.requestedLoadSize)
        callback.onResult(data ?: emptyList())
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Person>) {
        l("loadBefore-> key = ${params.key},loadSize = ${params.requestedLoadSize}")
    }

    override fun getKey(item: Person): Int {
        l("getKey-> ${item.index}")
        return item.index
    }

    fun showLoading() {
        l("正在加载中...")
    }

    fun hideLoading() {
        l("加载完成...")
    }

    fun l(s: String) {
        Log.e("TAG", s)
    }

}

class PersonPositionDataSource(val total: Int, val data: List<Person>) : PositionalDataSource<Person>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Person>) {
        val pos = computeInitialLoadPosition(params, total)
        val loadSize = computeInitialLoadSize(params, pos, total)
        val sublist = data.subList(pos, pos + loadSize)
        callback.onResult(sublist, pos, total)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Person>) {
        callback.onResult(data.subList(params.startPosition,
                params.startPosition + params.loadSize))
    }

}

class Repo {

    companion object {

        val sumData = ArrayList<Person>().apply {
            for (i in 0 until 500) {
                add(Person("第$i 个数据", i))
            }
        }

        //模拟item获取数据，key暂时认为是下标  0-400  获取 key-(key+pageSize)的数据
        fun getItemKeyData(key: Int, pageSize: Int): List<Person>? {
            val total = sumData.size
            //上一页最后一个数据的下标？？？？？？
            val prePage = if (key <= 0) {
                null
            } else {
                key - 1
            }
            //下一页的数据长度
            val actualKey = key + 1
            var nextPage = actualKey + pageSize
            if (nextPage > total - 1) {
                nextPage = total - 1
            }
            val data = ArrayList<Person>().apply {
                for (i in actualKey..nextPage) {
                    val person = sumData[i]
                    add(person)
                }
            }
            return data
        }

        //模拟page获取数据  传入key(假设代表页码)，返回key对应的集合及下一页的key
        fun getPageData(key: Int, pageSize: Int): PageData? {
            if (key <= 0) {
                return null
            }
            val total = sumData.size
            //下一页的数据长度
            var end = key * pageSize
            var start = end - pageSize
            if (start < 0) {
                start = 0
            }
            if (end > total - 1) {
                end = total - 1
            }
            val data = ArrayList<Person>().apply {
                for (i in start..end) {
                    val person = sumData[i]
                    add(person)
                }
            }
            return PageData(key + 1, data)
        }
    }

}


















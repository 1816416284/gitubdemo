var vuePage = {
    template:'<div class="page-bar" id="pager">\
	    <span class="btn btn-default" v-on:click="btnClick(1)">首页</span>\
	    <span class="btn btn-default" v-on:click="btnClick(Number(pageCurrent)-1)">上一页</span>\
		<span class="form-inline">第\
	    <input class="form-control" style="width:60px;text-align:center" v-model="pageCurrent" type="text" v-on:click="getMypageCurrent" v-on:blur="yzPage" v-on:keyup.enter="yzPage();btnClick(pageCurrent)" />\
		页</span>\
		<span>，共{{pages}}页</span>\
	    <span class="btn btn-default" v-on:click="btnClick(Number(pageCurrent)+1)">下一页</span>\
	    <span class="btn btn-default" v-on:click="btnClick(pagerData.page.pages)">尾页</span>\
	    <span class="form-inline">\
	        每页<select class="form-control" v-model="pagesize" number>\
	            <option v-for="item in pagerData.page.arrPageSize" :value="item">{{item}}</option>\
	        </select>\
	    </span>\
		<span>共{{pagerData.page.total}}条</span>\
	</div>',
    data:function(){
        return{
            mypagesize:10,
            mypageCurrent:1,//当前页
            sortparam:"",
            sorttype:1
        }
    },
    props: {
        pagerData:{
            default:function(){
                return{
                    page:{
                        //分页大小
                        //pageSize:10,
                        //分页数
                        //arrPageSize:[10,20,30,40],
                        //当前页面
                        //pageNum:1,
                        //总分页数
                        //pages:0,
                        //记录总数
                        //total:0
                    }
                }
            }

        },
        callback: {
            default: function() {
                return function callback() {
                    // todo
                }
            }
        }
    },
    methods:{
        btnClick:function(page) {
            if(page<=0){
                page = 1;
            }else if(page>this.pagerData.page.pages){
                page = this.pagerData.page.pages;
            }
            this.pagerData.page.pageNum = page;
            if (page != this.pagenum) {
                this.callback(page)
            }
        },
        yzPage:function(){
            if(/^[1-9]\d*$/.test(this.pageCurrent)==false){
                this.pageCurrent =this.mypageCurrent;
                //this.pagerData.page.pageNum = this.mypageCurrent;
            }else{
                if((Number(this.pageCurrent)<=0)||(Number(this.pageCurrent)>this.pagerData.page.pages)){
                    this.pageCurrent =this.mypageCurrent;
                    //this.pagerData.page.pageNum = this.mypageCurrent;
                }
            }

        },
        getMypageCurrent:function(){
            this.mypageCurrent = this.pageCurrent;
        }
    },
    computed:{
        // 分页大小 获取的时候显示父级传入的，修改的时候修改自身的。子组件不能修改父元素的值
        pagesize:{
            get:function(){
                return this.pagerData.page.pageSize;
            },
            set:function(value){
                this.pagerData.page.pageSize = value;
                this.pagerData.page.pageNum = 1;
                this.callback(1)
            }
        },
        pageCurrent:{
            get:function(){
                return this.pagerData.page.pageNum;
            },
            set:function(value){
                this.pagerData.page.pageNum = value;
            }
        },
        pages:{
            get:function () {
                this.pagerData.page.total%this.pagerData.page.pageSize==0?
                    this.pagerData.page.pages=this.pagerData.page.total/this.pagerData.page.pageSize:
                    this.pagerData.page.pages= Math.ceil(this.pagerData.page.total/this.pagerData.page.pageSize);
                 return this.pagerData.page.pages;
            },
        }
    }
}
new Vue({
    el: "#app",
    data: {
        // vue-page.js
        pagerData:{
            page:{
                //分页大小
                pageSize:5,
                //分页数
                arrPageSize:[5,10,20,30,40,50],
                //当前页面
                pageNum:1,
                //记录总数
                total:0,
            }
        },
        usermohu:{},
        entity: {addressOptionList:[],},
        list: [],
        ckids:[],
        City1List:[],
        City2List:[],
        City3List:[],
    },
    components:{
        'vue-page': vuePage,
    },
    methods: {dateFormat:function(time) {
            if(time!=null&&time!=''){
                //这个time是后台接收的参数，是个毫秒值，先把他变成js的时间
                var date=new Date(time);

                //date.getYear不可以用，他获取的是从1900年到现在的年份差，不能用
                var year=date.getFullYear();

                /* 在日期格式中，月份是从0开始的，因此要加  yyyy——MM-dd这的，1-9月份，前面要加一个0
                    要是月份加1
                 * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
                 * */
                var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
                var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
                var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
                var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
                var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
                // 拼接
                return year+"-"+month+"-"+day;
            }else{
                return "";
            }

        },
        findPage:function(){ //这个是vue-page.js分页插件里的change方法
             let _this=this;
            axios.post('../user/findPageAndCon.do?pageNum='+_this.pagerData.page.pageNum+"&pageSize="+_this.pagerData.page.pageSize,_this.usermohu).then(
                function (response) {
                      _this.pagerData.page.total=response.data.total;
                      _this.list=response.data.rows;
                })
        },
        findAll: function () {
            var url = "/user/findAll.do";
            var _this = this;
            axios.get(url).then(function (response) {
                console.log(response);
                _this.list = response.data;
            }).catch(function (err) {
                console.log(err)
            });
        },
        dele:function(){
            var _this = this;
            alert(_this.ckids)
            axios.post("/user/delete.do",_this.ckids).then(
                function (response) {
                    if(response.data.success){
                         alert(response.data.message);
                        _this.findPage();//刷新列表
                    }else{
                        alert(response.data.message);
                    }
            })
        },
        findOne:function(id){
            var _this = this;
          axios.post('user/findOne.do?uid='+id).then(
              function (response) {
                  _this.entity=response.data;
              })
        },
        save: function () {
            var _this = this;
            axios.post('../user/save.do',_this.entity).then(
                function (response) {
                    if(response.data.success){
                        alert(response.data.message);
                        //重新查询
                        _this.findPage();//重新加载
                    }else{
                        alert(response.data.message);
                    }

            }).catch(function (err) {
                console.log(err)
            });
        },
        //三级联动
        findCityByPid:function(pid){
            return axios.post('../user/findCityByPid.do?pid='+pid);
        },
        findCity2List:function(){
            var _this = this;
            _this.City2List=[];
            _this.City3List=[];
            _this.findCityByPid(_this.entity.aname1.id).then(
                function (response) {
                    _this.City2List=response.data;
                });
        },
        findCity3List:function () {
            var _this = this;
            _this.City3List=[];
            _this.findCityByPid(_this.entity.aname2.id).then(
                function (response) {
                    _this.City3List=response.data;
                });
        },
        pinjie:function () {
            var _this = this;
            var Str = _this.entity.aname1.cname+ _this.entity.aname2.cname+_this.entity.aname3.cname;
            _this.entity.addressOptionList.push({aname:Str});
        },
        //删除当前项
        deleTableRow:function (index) {
            var _this = this;
            _this.entity.addressOptionList.splice(index,1);
        },
    },
    created:function () {
          var _this = this;
         // this.findAll();
         //  this.pageHandler(1);
          this.findPage();
          this.findCityByPid(1).then(
              function (response) {
                  _this.City1List=response.data;
              });
      },
});
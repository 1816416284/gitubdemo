//服务层
app.service('userService',function($http){
    //用户信息分页展示
    this.findPage=function (pageNum,pageSize) {
        return $http.post('../user/findPage.do?pageNum='+pageNum+"&pageSize="+pageSize);
    }
    this.findUserAll=function(){
        return $http.post('../user/findAll.do');
    }
    //三级联动查询
    this.findCityByPid=function (pid) {
        return  $http.post('../user/findCityByPid.do?pid='+pid);
    }

    //查询实体
    this.findOne=function(id){
        return $http.post('../user/findOne.do?uid='+id);
    }
    //修改和添加
    this.save=function (entity) {
        return  $http.post('../user/save.do',entity);
    }
    //删除
    this.dele=function(ids){
        return $http.post('../user/delete.do',ids);
    }
});

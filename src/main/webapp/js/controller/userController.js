//控制层
app.controller('userController' ,function($scope,$controller,userService){

    $controller('baseController',{$scope:$scope});//继承

    $scope.findAll=function(){
        userService.findUserAll().success(
            function (response) {
                $scope.list=response;
            });
    }
    //分页查询
    $scope.search=function(page,rows){
        userService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }


    $scope.entity={addressOptionList:[]};
    //新增选项行
    $scope.addTableRow=function(){
        $scope.entity.addressOptionList.push({});
    }
    $scope.deleTableRow = function (index) {
        $scope.entity.addressOptionList.splice(index,1);
    }

    //三级联动
    $scope.findCityByPid=function(){
        userService.findCityByPid(1).success(
            function (response) {
             $scope.City1List=response;
        })
    }
    $scope.$watch('entity.aname1',function (newValue,oldValue) {
        $scope.City2List=[];
        $scope.City3List=[];
        userService.findCityByPid(newValue.id).success(function (response) {
            $scope.City2List = response;
        });
    })
    $scope.$watch('entity.aname2',function (newValue,oldValue) {
        $scope.City3List=[];
        userService.findCityByPid(newValue.id).success(function (response) {
            $scope.City3List = response;
        });
    })
    $scope.entity={aname:""};
    $scope.$watch('entity.aname3',function (newValue,oldValue) {
          var Str = $scope.entity.aname1.cname+ $scope.entity.aname2.cname+newValue.cname;
          $scope.entity.addressOptionList.push({aname:Str});
    })

   //回显实体
    $scope.findOne=function(id){
        userService.findOne(id).success(function (response) {
            $scope.entity= response;
        });
    }

   //修改新增
    $scope.save=function(){
        userService.save($scope.entity).success(
            function (response) {
            if(response.success){
                alert(response.message);
                //重新查询
                $scope.reloadList();//重新加载
            }else{
                alert(response.message);
            }
        })
    }
    //批量删除
    $scope.dele=function(){
        //获取选中的复选框
        userService.dele( $scope.selectIds ).success(
            function(response){
                if(response.success){
                    $scope.reloadList();//刷新列表
                }else{
                    alert(response.message);
                }
            }
        );
    }
});

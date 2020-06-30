app.controller('baseController' ,function($scope){
    $scope.selectIds=[];
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 5,
        perPageOptions: [5, 10, 20, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    }
    //是刷新的
    $scope.reloadList = function(){
         $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        //$scope.findPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }
    $scope.updateSelection = function($event,id){
        //用这个event来判断，当前是用户选中了，还是取消了，要是选中了，就要把这个id放进数组里面去，要是用户取消选择了，就需要把这个id从数组里面删掉
        if($event.target.checked){
            $scope.selectIds.push(id);
        }else{
            //取消的情况下，要把这个id在数组中删掉，数组没有直接按照值删除的方法，但是他有按照值来找这个值，对应的下标的方法，找到下标，用下标去删除
            var indexOf = $scope.selectIds.indexOf(id);
            //数组删除的方法，splice，需要两个参数，第一参数是角标，从什么地方开始删除，第二参数是删除几个数，肯定删除一个
            $scope.selectIds.splice(indexOf,1);
        }
    }
    $scope.JSONtoParse=function(jsonString,key){
        var json=JSON.parse(jsonString);//将json字符串转换为json对象
        var value="";
        for(var i=0;i<json.length;i++){
            if(i>0){
                value+=","
            }
            value+=json[i][key];
        }
        return value;
    }
});
new Vue({
    el:"#regiest",
    data:{
        code:"",
        Codetext: '获取验证码',
        entity:{},
    },
    methods:{
        resetPasswd(){
            let _this = this;
            if(_this.entity.newPassword!=null&&_this.entity.newPassword!=""){
                if(_this.entity.newPassword==_this.entity.twoPassword){
                     axios.post("user/resetPasswd.do?newPassword="+_this.entity.newPassword).then(
                         function (response) {
                             if(response.data.success){
                                 alert(response.data.message)
                                 window.location.href="login.html";
                             }else{
                                 alert(response.data.message);
                             }
                     })
                }else{
                    alert("两次密码不一致")
                }
            }else{
                alert("密码不能为空")
            }
        },
        identity(){
            let _this = this;
            axios.post("user/identity.do",_this.entity).then(
                function (response) {
                    if(response.data.success){
                        alert(response.data.message)
                        window.location.href="resetpassword.html";
                    }else{
                        alert(response.data.message);
                    }
                })
        },
        register(){
            let _this = this;
            axios.post("user/register.do",_this.entity).then(
                function (response) {
                    if(response.data.success){
                        alert(response.data.message)
                        window.location.href="login.html";
                    }else{
                        alert(response.data.message);
                    }
                })
        },
        identityCode(){ //验证身份获取验证码
            let _this = this;
            if(_this.entity.tel!=null&&_this.entity.tel.length==11){
                axios.post("user/identityCode.do?tel="+_this.entity.tel).then(
                    function (response) {
                        if(!response.data.success){
                            alert(response.data.message);
                        }else{
                            console.log(response.data.message)
                        }
                    })
                _this.CountTime();
            }else{
                alert("必须是11位数手机号码且不能为空")
            }
        },
        getCode() {//注册获取验证码
            let _this = this;
           if(_this.entity.tel!=null&&_this.entity.tel.length==11){
                axios.post("user/getCode.do?tel="+_this.entity.tel).then(
                    function (response) {
                     if(!response.data.success){
                         alert(response.data.message);
                     }else{
                         console.log(response.data.message)
                     }
                })
                 _this.CountTime();
           }else{
               alert("必须是11位数手机号码且不能为空")
           }
        },
        CountTime(){//验证码定时器
            let _this = this;
            let time = 30;
            this.$refs.code.disabled=true;
            let interval = setInterval(() => {
                if (time==0) {
                    _this.Codetext="获取验证码";
                    this.$refs.code.disabled=false
                    time=30;
                    axios.post("user/deleteCode.do?tel="+_this.entity.tel);
                    clearInterval(interval);
                } else {
                    _this.Codetext="重新发送"+time;
                    time--;
                }
            }, 1000);
        },
    }
})
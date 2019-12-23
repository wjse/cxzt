(function(){
    $(function(){
        $("#left-username").text(window.sessionStorage.getItem("nickName"));

        api("/menu" , "GET" , null , function(res){
            if(res && res.menus){
                let html = "";
                for(i in res.menus){
                    let menu = res.menus[i];
                    html += "<li>";
                    html += "<a href='" + menu.href + "' target='main'>";
                    html += "<i class='icon-"+ menu.icon +"'></i>";
                    html += menu.name;
                    html += "</a></li>";
                }
                $("#side-main-menu").html(html);
            }
        });

        $("#passwordSubmit").on("click" , function () {
            $("#form").on("submit" , () => false);
            let password = $("#password").val();
            let confirm = $("#confirmPassword").val();
            if(!password || !confirm){
                return;
            }
            if(password != confirm){
                alert("两次密码输入不同，请重新输入");
                $("#password").val("");
                $("#confirmPassword").val("");
                return;
            }

            api("/user/password" , "PUT" ,{password : sha1(password)} , function(res){
                if(res.code == 200){
                    alert("密码修改成功");
                    $("#passwordModal").modal("hide");
                    window.sessionStorage.clear();
                    window.location.href = "/login.html";
                }else{
                    alert("密码修改失败");
                }
            });
        });
    });

    $("#logout").on("click" , () => {
        window.sessionStorage.clear();
        window.location.href = "/login.html";
    });
})($)
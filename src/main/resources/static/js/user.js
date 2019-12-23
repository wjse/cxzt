(function(){
    const pageSize = 10;

    $(function(){
        loadUsers(1);
        $("#search").on("click" , function(){
            loadUsers(1);
        });
    });

    function loadUsers(pageNum){
        let params = {};

        let nickName = $("#searchNickName").val();
        let mobile = $("#searchMobile").val();
        if(nickName){
            params.nickName = nickName;
        }

        if(mobile){
            params.mobile = mobile;
        }

        api("/user?pageNum=" + pageNum + "&pageSize=" + pageSize , "GET" , params , function(res){
            if(res && res.code == 200){
                render(res.data);
            }
        });
    };

    function render(page){
        P.initMathod({
            params: {elemId: '#page' , total : page.total , pageIndex: page.pageNum , pageNum : page.pages , pageSize : page.pageSize},
            requestFunction: function () {}
        });

        let list = page.list;
        if(!list || list.length == 0){
            $("#tbody").html("");
            return;
        }

        let html = "";
        for(let i = 0 ; i < list.length ; i++){
            let user = list[i];
            html += "<tr>";
            html += "<th scope='row'>"+ (i + 1) +"</th>";
            html += "<td>"+ user.username +"</td>";
            html += "<td>"+ user.nickName +"</td>";
            html += "<td>"+ user.mobile +"</td>";
            html += "<td>"+ user.email +"</td>";
            html += "<td>"+ (user.type == 'NORMAL' ? '扫票用户' : '管理员') +"</td>";
            html += "<td>"+ user.region +"</td>";
            html += "<td>"+ user.company +"</td>";
            html += "<td><button class='btn btn-warning reset' id='"+ user.id +"'>重置密码</button></td>";
            html += "</tr>";
        }

        $("#tbody").html(html);
        $(".reset").on("click" , function(){
            if(confirm("确定重置该用户密码？")){
                api("/user/password/" + $(this).attr("id") , "PUT" , null , function(res){
                    if(res.code == 200){
                        alert("重置密码成功");
                    }else{
                        alert("重置密码失败");
                    }
                });
            }
        });
    };
})($)
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
    });

    $("#logout").on("click" , () => {
        window.sessionStorage.clear();
        window.location.href = "/login.html";
    });
})($)
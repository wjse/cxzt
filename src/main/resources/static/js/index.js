(function(){
    $(function(){
        $("#left-username").text(window.sessionStorage.getItem("username"));
    });

    $("#logout").on("click" , () => {
        window.sessionStorage.clear();
        window.location.href = "/login.html";
    });
})($)
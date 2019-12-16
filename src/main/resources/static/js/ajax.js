function api(path , type , data , success , headers){
    type = type.toUpperCase();
    if(data && (type == "GET" || type == "DELETE")){
        for(let k in data){
            let s = "&";
            if(path.indexOf("?") == -1){
                s = "?";
            }

            path += s + k + "=" + data[k];
        }
    }
    let options = {
        url : path,
        dataType : "json",
        contentType : "application/json",
        type : type,
        beforeSend: function(request) {
            let token = window.sessionStorage.getItem("token");
            if(token){
                request.setRequestHeader("Token" , token);
            }
            if(headers){
                for(let k in headers){
                    request.setRequestHeader(k , headers[k]);
                }
            }
        },
        success : function(res , status , response){
            let token = response.getResponseHeader("Token");
            if(token){
                window.sessionStorage.setItem("token" , token);
            }
            success.call(this , res);
        },
        error : function(e){
            if(e.status == 401){
                window.location.href = "/login.html"
            }
        }
    };


    if(data && (type == "POST" || type == "PUT")){
        options.data = JSON.stringify(data);
    }
    $.ajax(options);
};

Date.prototype.format = function(fmt)
{ //author: meizz
  var o = {
    "M+" : this.getMonth()+1,                 //月份
    "d+" : this.getDate(),                    //日
    "h+" : this.getHours(),                   //小时
    "m+" : this.getMinutes(),                 //分
    "s+" : this.getSeconds(),                 //秒
    "q+" : Math.floor((this.getMonth()+3)/3), //季度
    "S"  : this.getMilliseconds()             //毫秒
  };
  if(/(y+)/.test(fmt))
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
  for(var k in o)
    if(new RegExp("("+ k +")").test(fmt))
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
  return fmt;
}
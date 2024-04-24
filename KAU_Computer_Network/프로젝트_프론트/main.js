var lotto =[];
var input_num=[];
var dup=false;
var over=false;

function rand(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

window.onload=function(){
    for(var i=0; i<7; i++){
        lotto[i] = rand(1,35);
        for(var j=0; j<i; j++){
            if(lotto[i]==lotto[j]) {
                i--;
                break;
            }
        }
        console.log(lotto[i]);
    }
}

function sub(){
    var cnt=0;
    input_num[0]=document.getElementById("val1").value;
    input_num[1]=document.getElementById("val2").value;
    input_num[2]=document.getElementById("val3").value;
    input_num[3]=document.getElementById("val4").value;
    input_num[4]=document.getElementById("val5").value;
    input_num[5]=document.getElementById("val6").value;
    input_num[6]=document.getElementById("val7").value;
    for(var i=0; i<7; i++){
        for(var j=0; j<7; j++){
            if(input_num[i]==lotto[j]) cnt++;
            if(i!=j&&input_num[i]==input_num[j]) dup=true;
        }
        if(input_num[i]>35 || input_num[i]<1||input_num[i]==null) over=true;
    }
    if(dup&&!over){
        alert("중복된 수가 입력되었습니다.");
        dup=false;
    }
    else if(over){
        alert("1에서 35까지의 수를 입력해주세요.");
        over=false;
    }
    else{
        alert(8-cnt+"등");
        ref();
    }
}

function ref(){
    console.clear();
    for(var i=0; i<7; i++){
        lotto[i]=rand(1,35);
        console.log(lotto[i]);
        input_num[i]=0;
    }
    document.getElementById("val1").value=null;
    document.getElementById("val2").value=null;
    document.getElementById("val3").value=null;
    document.getElementById("val4").value=null;
    document.getElementById("val5").value=null;
    document.getElementById("val6").value=null;
    document.getElementById("val7").value=null;
}

function auto(){
    var cnt=0;
    for(var i=0; i<7; i++){
        input_num[i] = rand(1,35);
        for(var j=0; j<i; j++){
            if(input_num[j]==input_num[i]){
                i--;
                break;
            }
        }
        for(var j=0; j<7; j++){
            if(input_num[i]==lotto[j]) cnt++;
        }
    }
    document.getElementById("val1").value=input_num[0];
    document.getElementById("val2").value=input_num[1];
    document.getElementById("val3").value=input_num[2];
    document.getElementById("val4").value=input_num[3];
    document.getElementById("val5").value=input_num[4];
    document.getElementById("val6").value=input_num[5];
    document.getElementById("val7").value=input_num[6];
    setTimeout(function(){alert(8-cnt+"등");}, 10);
}


function chatSub(){
    var name = document.getElementById("name");
    var text = document.getElementById("text");
    var body = document.getElementById("sent");
    var subname = document.getElementById("subname");
    var subtext = document.getElementById("subtext");

    if(name.value==""){
        alert("이름을 입력해주세요.");
    }
    else if(text.value==""){
        alert("내용을 입력해주세요.");
    }
    else{
        subname.innerHTML = name.value;
        subtext.innerHTML = text.value;
        document.getElementById("text").value=null;
        var nextName = subname.cloneNode();
        var nextText = subtext.cloneNode();
        body.append(nextName);
        body.append(nextText);

        document.querySelector('#subname').style.color="white";
        document.querySelector('#subtext').style.color="#FCD153";
        document.querySelector('#subname').style.textAlign="right";
        document.querySelector('#subtext').style.textAlign="right";
        document.querySelector('#subname').style.marginRight="10px";
        document.querySelector('#subtext').style.marginRight="10px";
        //document.querySelector('#subtext').style.marginBottom="5px";
        document.querySelector('#subname').style.fontSize="15px";
        document.querySelector('#subtext').style.fontSize="25px";

        document.querySelector('#subname').removeAttribute('id');
        document.querySelector('#subtext').removeAttribute('id');
    }

    if (log.isScrollBottom) {
      log.scrollTop = log.scrollHeight;
    }
}

function chatRcv(){
    var name = document.getElementById("name");
    var text = document.getElementById("text");
    var body = document.getElementById("sent");
    var subname = document.getElementById("subname");
    var subtext = document.getElementById("subtext");

    if(name.value==""){
        alert("이름을 입력해주세요.");
    }
    else if(text.value==""){
        alert("내용을 입력해주세요.");
    }
    else{
        subname.innerHTML = name.value;
        subtext.innerHTML = text.value;
        document.getElementById("text").value=null;
        document.getElementById("name").value=null;
        var nextName = subname.cloneNode();
        var nextText = subtext.cloneNode();
        body.append(nextName);
        body.append(nextText);

        document.querySelector('#subname').style.color="#FCD153";
        document.querySelector('#subtext').style.color="white";
        document.querySelector('#subname').style.textAlign="left";
        document.querySelector('#subtext').style.textAlign="left";
        document.querySelector('#subname').style.fontSize="15px";
        document.querySelector('#subtext').style.fontSize="25px";
        document.querySelector('#subtext').style.marginBottom="10px";

        document.querySelector('#subname').removeAttribute('id');
        document.querySelector('#subtext').removeAttribute('id');
    }
}

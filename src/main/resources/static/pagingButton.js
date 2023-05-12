"use strict"


let page;
var prevLink = $('#pre');


document.addEventListener('DOMContentLoaded', function() {
  var offset = $('#offset').val();
  console.log(offset)
  // ボタン要素を取得し、非表示にする
  if(offset==="0"){
	  $(".pre").hide();
  }else{
	  $(".pre").show();
	  
  }
});





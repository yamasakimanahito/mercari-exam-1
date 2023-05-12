'use strict';
 
 $(function() {
  // 親セレクトボックスの値が変更された時の処理
  $('#parentCategory').on('change',function() {
	  console.log(1)
    // 選択された親のIDを取得
    var parentId = $(this).val();
    // 子セレクトボックスを空にする
    $('#childCategory').empty();
    $('#childCategory').append($('<option>').val('').text('childCategory'));
    // 孫セレクトボックスを空にする
    $('#grandChildCategory').empty();
    $('#grandChildCategory').append($('<option value=0>').val('').text('-grandChildCategory-'));

    // 親が選択されていない場合は、処理を終了
    if (!parentId) {
      return;
    }

    // 子セレクトボックスを取得するためのURLを作成
    var url = 'http://localhost:8080/exam-mercari/getChildren?parentId=' + parentId;
    // ajaxで子セレクトボックスの内容を取得
    $.get(url, function(data) {
      // 取得したデータをセット
      $('#childCategory').html(data);
    });
  });

  // 子セレクトボックスの値が変更された時の処理
  $('#childCategory').change(function() {
    // 選択された子のIDを取得
    var childId = $(this).val();
    console.log(childId);
    // 孫セレクトボックスを空にする
    $('#grandChildCategory').empty();

    // 子が選択されていない場合は、処理を終了
    if (!childId) {
      return;
    }

    // 孫セレクトボックスを取得するためのURLを作成
    var url = 'http://localhost:8080/exam-mercari/getGrandchildren?childId=' + childId;
    // ajaxで孫セレクトボックスの内容を取得
    $.get(url, function(data) {
      // 取得したデータをセット
      $('#grandChildCategory').html(data);
    });
  });
});
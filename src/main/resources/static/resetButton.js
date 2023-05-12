'use strict'

//検索欄の一括リセット
$(function() {
	$('#reset').on('click', function() {
		$('#name').val('');
		$('#parentCategory').val('0')
		$('#childCategory').empty();
		$('#childCategory').append($('<option>').val('').text('- choose parentCategory -'));
		// 孫セレクトボックスを空にする
		$('#grandChildCategory').empty();
		$('#grandChildCategory').append($('<option value=0>').val('').text('- choose childCategory -'));
		$('#brand').val('');


	})
});

'use strict'

$(function(){
	$('#reset').on('click',function(){
		// セレクトボックスの要素を取得


		$('#inputName').val('');
		$('#price').val('');
		$('#parentCategory').val('0')
		$('#childCategory').empty()
		$('#childCategory').append($('<option>').val('').text('- choose parentCategory -'));
		$('#grandChildCategory').empty();
		$('#grandChildCategory').append($('<option value=0>').val('').text('- choose childCategory -'));
		$('#brand').val('');
		for (var i = 1; i <4 ; i++) {
		$('#condition'+i).prop('checked',false)
		}
		$('#description').val('')
		
	})
})
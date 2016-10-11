function invokeServerMethod() {

	// using wff utf-8 encoder which is cross browser
	var uint8Array = wffGlobal.encoder.encode("こんにちは webfirmframework");
	//if the bytes are some row bytes like bytes from some binary file
	//then use Int8Array
//	var int8Array = new Int8Array(stringBytes);

	console.log('object type', Object.prototype.toString
			.call(uint8Array));
	console.log(uint8Array);
	
	var argument = {
		'nullArray' : [null, null],
		'undefinedArray' : [undefined, undefined],
		'nullVal' : null,
		'undefinedVal' : undefined,
		'somekey' : 'some value',
		'string' : 'string value',
		'numb' : 55555,
		'bool' : true,
		'regex' : /ab+c/,
		'anObj' : {
			'key' : 'val'
		},
		'byteArray' : uint8Array,
		'funcArray' : function() {console.log('m function');},
		'numberArray' : [ 5, 55, 555, 55, 5555 ]
	};

	wffAsync.serverMethod('testServerMethod', argument).invoke(
			function(obj) {

				console.log('callback obj ', obj);

				for (key in obj) {
					console.log('key is ' + key + ' ', obj[key]);
				}

				console.log('callback byteArray ', obj.byteArray);

				// using wff utf-8 decoder which is cross browser
				console.log('callback byteArray '
						+ wffGlobal.decoder.decode(obj.byteArray));

				obj.testFun('hi how are you');
			});
};

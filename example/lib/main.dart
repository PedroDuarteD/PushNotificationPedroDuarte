import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:push_notification_pedro_duarte/push_notification_pedro_duarte.dart';

void main() {
  runApp( MyApp());
}

class MyApp extends StatefulWidget {
  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {



initPlatformState() async {
    try {

     // await PushNotificationPedroDuarte.notification_push("Titulo","Descricao");
await PushNotificationPedroDuarte.notification_push_event();
    } on Exception {
    print("Algo deu erro não estava previsto !");
    }

  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: ElevatedButton(
            onPressed: ()async{
   await initPlatformState();
    },
    child: Text("Ola")

          ),
        ),
      ),
    );
  }
}

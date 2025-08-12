import 'package:flutter/foundation.dart' show kIsWeb;
import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'جي jeeey',
      debugShowCheckedModeBanner: false,
      home: const WebViewHome(),
    );
  }
}

class WebViewHome extends StatefulWidget {
  const WebViewHome({super.key});

  @override
  State<WebViewHome> createState() => _WebViewHomeState();
}

class _WebViewHomeState extends State<WebViewHome> {
  late final WebViewController _controller;
  bool _isLoading = true; // ملاحظة: ليست final لكي نغيّرها بالـ setState
  final String initialUrl = 'https://web.jeeey.net/';

  @override
  void initState() {
    super.initState();

    _controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setNavigationDelegate(
        NavigationDelegate(
          onPageStarted: (url) => setState(() => _isLoading = true),
          onPageFinished: (url) => setState(() => _isLoading = false),
        ),
      )
      ..loadRequest(Uri.parse(initialUrl));
  }

  Future<bool> _onWillPop() async {
    try {
      if (!kIsWeb && await _controller.canGoBack()) {
        await _controller.goBack();
        return false;
      }
    } catch (_) {}
    return true;
  }

  @override
  Widget build(BuildContext context) {
    if (kIsWeb) {
      return Scaffold(
        appBar: AppBar(title: const Text('Jeeey — Open in browser')),
        body: Center(
          child: ElevatedButton(
            onPressed: () {
              // لاحقاً: اضف url_launcher لفتح المتصفح إن أردت
            },
            child: const Text('افتح الموقع في المتصفح'),
          ),
        ),
      );
    }

    return SafeArea(
      child: WillPopScope(
        onWillPop: _onWillPop,
        child: Scaffold(
          body: Column(
            children: [
              if (_isLoading) const LinearProgressIndicator(minHeight: 3),
              Expanded(child: WebViewWidget(controller: _controller)),
            ],
          ),
        ),
      ),
    );
  }
}

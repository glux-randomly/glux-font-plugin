![](https://badgen.net/badge/latest%20version/0.0.7/green) ![](https://badgen.net/badge/license/Apache%202.0/blue)

This is an Android plugin that helps you automatically generate font resources like `<string>` for `fontPath` and `<style>` for `android:TextAppearance`

**Note: Combine use with https://github.com/glux-randomly/glux-font-compiler to create constant paths class and utils class from fonts to use in programming**

#### Result

If you have a font is `OpenSans-Semibold.ttf`, output is like this
```
<resources>
    <string name="path.opensans.semibold" translatable="false">fonts/OpenSans-Semibold.ttf</string>
    <style name="TextAppearance.FontPath.OpenSans.Semibold" parent="android:TextAppearance">
        <item name="fontPath">@string/path.opensans.semibold</item>
        <item name="android:includeFontPadding">false</item>
    </style>
</resources>
```

#### How to use
Add to project build.gradle
```
classpath "dv.trung.glux:glux-font-plugin:0.0.7"
```

Add to app build.gradle (at top)
```
apply plugin: 'dv.trung.glux.glux-font'
```

Rebuild project

#### Format font file
- Put you fonts to `assets/fonts/`
- Format is `FontName-FontStyle.ext`
Ex: `OpenSans-Bold.ttf`


License
-------

    Copyright 2020 QuocTrung

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


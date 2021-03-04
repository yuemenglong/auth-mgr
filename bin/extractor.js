const ts = require('typescript');
const fs = require('fs');
const _ = require("lodash")

const code = fs.readFileSync('D:/workspace/wj/xxdd-web/pages/index.tsx', 'utf8') // sourceText
const root = ts.createSourceFile(
    'x.ts',   // fileName
    code,
    ts.ScriptTarget.Latest // langugeVersion
);

function nSpace(n) {
    return _.range(n).map(x => " ").join("")
}

function walk(node, fn, level) {
    level = level || 0
    fn(node, level)
    node.forEachChild(child => {
        walk(child, fn, level + 1)
    })
}

function displayKindLoop(node) {
    walk(node, (node, level) => {
        let ks = ts.SyntaxKind[node.kind]
        console.log(nSpace(level * 2) + ks)
    })
    // level = level || 0
    // node.forEachChild(child => {
    //     displayNodeLoop(child, level + 1)
    // })
}


walk(root, (node, level) => {
    let ks = ts.SyntaxKind[node.kind]
    if (ks == "CallExpression" && ts.SyntaxKind[node.expression.kind] == "Identifier") {
        // console.log(node.expression.escapedText)
        console.log(node.arguments[0].text)
    }
})

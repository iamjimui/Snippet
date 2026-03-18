import { LanguageModel } from "./languages.model";

export interface SnippetSheatModel {
    id: string,
    language: LanguageModel;
    content: string;
    name: string;
}
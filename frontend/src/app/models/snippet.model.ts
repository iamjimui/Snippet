import { SnippetSheatModel } from "./snippetSheatModel";

export interface SnippetModel {
    title: string,
    description: string,
    sheets: SnippetSheatModel[],
    tags: string[],
    visible: boolean,
    bookmarkedBy: string[],
    nbComments: number;
    userEmail: string,
    createdAt?: string,
    updatedAt?: string,
    id?: string
    comments: CommentModel[],
}

export interface CommentModel {
    content: string,
    userEmail: string,
    snippetId: string,
    createdAt: string,
    updatedAt?: string,
    id: string,
}

export enum Menu {
    ALL_SNIPPET,
    MY_SNIPPET,
    FAV_SNIPPET
}
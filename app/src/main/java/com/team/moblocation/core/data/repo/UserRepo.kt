package com.team.moblocation.core.data.repo

class UserRepo: IUserRepo {
	override fun getUser(): String {
		return "Hello User"
	}
}